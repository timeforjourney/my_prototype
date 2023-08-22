package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import com.mytest.app.repository.SearchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Service
class SearchService(
	val searchRepository: SearchRepository
) {

	private val logger = LoggerFactory.getLogger(this::class.java)

	/**
	 *
	 */
	fun searchKakaoResult(keyword: String, page: String, size: String): Mono<KakaoResponse> {

		return searchRepository.getKakaoSearchResult(keyword, page, size)
	}


	/**
	 * 네이버 검색 API
	 */
	fun searchNaverResult(keyword: String, page: String, size: String): Mono<NaverResponse> {

		return searchRepository.getNaverSearchResult(keyword, page, size)
	}


	fun sortSearchResult(keyword: String, page: String, size: String): Mono<SearchResult> {

		val kakaoList = searchRepository.getKakaoSearchResult(keyword, page, size)
			.map { it.documents }

		val naverList = searchRepository.getNaverSearchResult(keyword, page, size)
			.map { it.items }

		val commonList = Mono.zip(kakaoList, naverList)
			.map {
				it.t1.map { it.placeName }
					.intersect(it.t2.map { it.title })
			}
			.flatMap { Mono.just(it) }

		val kakaos = Mono.zip(kakaoList, commonList)
			.map {
				it.t1.map { it.placeName }
					.subtract(it.t2)
			}
			.flatMap { Mono.just(it) }

		val navers = Mono.zip(naverList, commonList)
			.map {
				it.t1.map { it.title }
					.subtract(it.t2)
			}
			.flatMap { Mono.just(it) }

		val mergeValues = Mono.zip(commonList, kakaos, navers)
			.map { it.t1.plus(it.t2).plus(it.t3) }
			.flatMap { Mono.just(it) }


		val result = mergeValues
			.subscribeOn(Schedulers.parallel())
			.flatMap { titles ->
			val titleObjects = titles.map { it?.let { it1 -> SearchResult.Title(it1) } }
			Mono.just(SearchResult(titleObjects))
		}

		return result

	}
}

