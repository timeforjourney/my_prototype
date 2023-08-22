package com.mytest.app.repository

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Repository
class SearchRepository(@Qualifier("customWebClient") private val webClient: WebClient) {

	private val log = org.slf4j.LoggerFactory.getLogger(SearchRepository::class.java)

	@Value("\${openapi.kakao.url}")
	lateinit var kakaoApiUrl: String

	@Value("\${openapi.kakao.auth}")
	lateinit var kakaoApiAuth: String

	@Value("\${openapi.naver.url}")
	lateinit var naverUrl: String

	@Value("\${openapi.naver.client-id}")
	lateinit var naverClientId: String

	@Value("\${openapi.naver.client-secret}")
	lateinit var naverClientSecret: String

	@Value("\${openapi.naver.host}")
	lateinit var naverHost: String


	/**
	 * 카카오 검색 API
	 */
	fun getKakaoSearchResult(keyword: String, page: String, size: String): Mono<KakaoResponse> {

		val customWebClient = webClient.mutate()
			.baseUrl(kakaoApiUrl)
			.defaultHeader(HttpHeaders.AUTHORIZATION, kakaoApiAuth)
			.build()

		return customWebClient.get()
			.uri { uriBuilder ->
				uriBuilder
					.queryParam("query", keyword)
					.queryParam("page", page)
					.queryParam("size", "5")
					.build()
			}
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(KakaoResponse::class.java)
			.subscribeOn(Schedulers.parallel())
			.onErrorResume { Mono.error(it) }
	}


	/**
	 * 네이버 검색 API
	 */
	fun getNaverSearchResult(keyword: String, page: String, size: String): Mono<NaverResponse> {

		val headers = mapOf<String, String>(
			"Host" to naverHost,
			"X-Naver-Client-Id" to naverClientId,
			"X-Naver-Client-Secret" to naverClientSecret)


		val customWebClient = webClient.mutate()
			.baseUrl(naverUrl)
			.defaultHeaders { header -> header.setAll(headers) }
			.build()

//		val encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8")
		return customWebClient.get()
			.uri { uriBuilder ->
				uriBuilder
					.queryParam("query", keyword)
					.queryParam("start", page)
					.queryParam("display", "5")
					.build()
			}
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(NaverResponse::class.java)
			.subscribeOn(Schedulers.parallel())
			.onErrorResume { Mono.error(it) }
	}

}