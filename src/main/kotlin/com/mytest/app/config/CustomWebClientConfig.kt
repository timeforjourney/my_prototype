package com.mytest.app.config

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.util.function.Consumer


@Configuration
class CustomWebClientConfig {

	private val log = LoggerFactory.getLogger(CustomWebClientConfig::class.java)

	@Qualifier("customWebClient")
	@Bean
	fun customWebClient(): WebClient {

		val exchangeStrategies = ExchangeStrategies.builder()
			.codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50) }
			.build()

		/**
		 * Debug 레벨 일 때 form Data 와 Trace 레벨 일 때 header 정보는 민감한 정보를 포함하고 있기 때문에,
		 * 기본 WebClient 설정에서는 정보를 로그에서 확인할 수 가 없습니다.
		 * 개발 진행 시 Request/Response 정보를 상세히 확인하기 위해서는
		 * ExchageStrateges 와 logging level 설정을 통해 로그 확인이 가능하도록 해 주는 것이 좋습니다.
		 */
		exchangeStrategies
			.messageWriters()
			.stream()
			.filter { obj: HttpMessageWriter<*>? -> LoggingCodecSupport::class.java.isInstance(obj) }
			.forEach { writer: HttpMessageWriter<*> -> (writer as LoggingCodecSupport).isEnableLoggingRequestDetails = true }

		return WebClient.builder()
			.clientConnector(
				ReactorClientHttpConnector(
					HttpClient
						.create()
						.secure { sslContextSpec ->
							sslContextSpec.sslContext(
								SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
							)
						}
						.responseTimeout(java.time.Duration.ofMillis(180000))
				)
			)
			.exchangeStrategies(exchangeStrategies)
			.filter(ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
				log.debug("--> Request: {} {}", clientRequest.method(), clientRequest.url())
				clientRequest.headers()
					.forEach { name: String?, values: List<String?> ->
						values.forEach(Consumer<String?> { value: String? -> log.debug("{} : {}", name, value) })
					}

				Mono.just<ClientRequest>(clientRequest)
			})
			.filter(ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
				log.debug("--> Response: {}", clientResponse.statusCode())
				clientResponse.headers().asHttpHeaders()
					.forEach { name: String?, values: List<String?> ->
						values.forEach(Consumer<String?> { value: String? -> log.debug("{} : {}", name, value) })
					}

				Mono.just<ClientResponse>(clientResponse)
			})
			.build()
	}
}