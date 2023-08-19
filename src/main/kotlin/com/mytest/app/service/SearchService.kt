package com.mytest.app.service

import com.mytest.app.repository.KakaoSearchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class SearchService (
		val kakaoSearchRepository: KakaoSearchRepository
) {

	private val log = LoggerFactory.getLogger(this::class.java)

	fun searchKakaoResult() : Mono<String> {

//		log.info("searchKakaoResult")
		return kakaoSearchRepository.getKakaoSearchResult()
	}
}