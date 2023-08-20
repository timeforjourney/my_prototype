package com.mytest.app.service

import com.mytest.app.repository.SearchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class SearchService (
		val searchRepository: SearchRepository
) {

	private val log = LoggerFactory.getLogger(this::class.java)

	fun searchKakaoResult(keyword : String, page: String, size : String) : Mono<String> {

		return searchRepository.getKakaoSearchResult(keyword, page, size)
	}



	fun searchNaverResult(keyword : String, page: String, size : String) : Mono<String> {

//		log.info("searchKakaoResult")
		return searchRepository.getNaverSearchResult(keyword, page, size)
	}
}