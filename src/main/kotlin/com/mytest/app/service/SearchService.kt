package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.repository.SearchRepository
import com.mytest.app.util.UtilService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class SearchService(
	val searchRepository: SearchRepository,
	val utilService: UtilService
) {

	private val log = LoggerFactory.getLogger(this::class.java)

	fun searchKakaoResult(keyword: String, page: String, size: String): Mono<KakaoResponse> {

		return searchRepository.getKakaoSearchResult(keyword, page, size)
	}


	fun searchNaverResult(keyword: String, page: String, size: String): Mono<NaverResponse> {


		val result = searchRepository.getNaverSearchResult(keyword, page, size)

//		removeHtmlTag(result)

		return searchRepository.getNaverSearchResult(keyword, page, size)
	}
}