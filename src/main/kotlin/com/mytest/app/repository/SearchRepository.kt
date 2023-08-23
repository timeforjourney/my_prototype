package com.mytest.app.repository

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.constant.CountConstant
import com.mytest.app.domain.constant.SearchConstant
import kotlinx.coroutines.coroutineScope
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
	suspend fun getKakaoSearchResult(keyword: String, page: String, size: String): Mono<KakaoResponse> = coroutineScope {

		val customWebClient = webClient.mutate()
			.baseUrl(kakaoApiUrl)
			.defaultHeader(HttpHeaders.AUTHORIZATION, kakaoApiAuth)
			.build()

		return@coroutineScope customWebClient.get()
			.uri { uriBuilder ->
				uriBuilder
					.queryParam(SearchConstant.SEARCH_QUERY.param, keyword)
					.queryParam(SearchConstant.KAKAO_SEARCH_PAGE.param, page)
					.queryParam(SearchConstant.KAKAO_SEARCH_PAGE_SIZE.param, CountConstant.SEARCH_SIZE_VALUE.count)
					.build()
			}
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(KakaoResponse::class.java)
			.subscribeOn(Schedulers.parallel())
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it) }
	}


	/**
	 * 네이버 검색 API
	 */
	suspend fun getNaverSearchResult(keyword: String, page: String, size: String): Mono<NaverResponse> = coroutineScope {

		val headers = mapOf<String, String>(
			SearchConstant.NAVER_SEARCH_HOST.param to naverHost,
			SearchConstant.NAVER_SEARCH_CLIENTID.param to naverClientId,
			SearchConstant.NAVER_SEARCH_CLIENTSECRET.param to naverClientSecret
		)

		val customWebClient = webClient.mutate()
			.baseUrl(naverUrl)
			.defaultHeaders { header -> header.setAll(headers) }
			.build()


		return@coroutineScope customWebClient.get()
			.uri { uriBuilder ->
				uriBuilder
					.queryParam(SearchConstant.SEARCH_QUERY.param, keyword)
					.queryParam(SearchConstant.NAVER_SEARCH_PAGE.param, page)
					.queryParam(SearchConstant.NAVER_SEARCH_PAGE_SIZE.param, CountConstant.SEARCH_SIZE_VALUE.count)
					.build()
			}
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(NaverResponse::class.java)
			.subscribeOn(Schedulers.parallel())
			.onErrorResume { Mono.error(it) }
			.switchIfEmpty(Mono.empty())

	}

}