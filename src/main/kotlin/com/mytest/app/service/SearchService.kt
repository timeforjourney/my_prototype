package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import com.mytest.app.repository.DataRedisRepository
import com.mytest.app.repository.SearchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Service
class SearchService(
	val searchRepository: SearchRepository,
	val dataRedisRepository: DataRedisRepository
) {

	private val logger = LoggerFactory.getLogger(this::class.java)

	/**
	 *
	 */
	suspend fun searchKakaoResult(keyword: String, page: String, size: String): Mono<KakaoResponse> {

		return searchRepository.getKakaoSearchResult(keyword, page, size)
	}


	/**
	 * 네이버 검색 API
	 */
	suspend fun searchNaverResult(keyword: String, page: String, size: String): Mono<NaverResponse> {

		return searchRepository.getNaverSearchResult(keyword, page, size)
	}


	suspend fun sortSearchResult(keyword: String, page: String, size: String): Mono<SearchResult> {

		val kakaoList = searchRepository.getKakaoSearchResult(keyword, page, size)
			.map { it.documents }

		val naverList = searchRepository.getNaverSearchResult(keyword, page, size)
			.map { it.items }

		val mergeValues = mergeValuesFromWebClient(kakaoList, naverList)

		logger.debug("--------> getCount start")

		dataRedisRepository.get(keyword)
			.flatMap { countValue ->
				val newCount = (countValue.toString().toIntOrNull() ?: 0) + 1
				dataRedisRepository.save(keyword, newCount).thenReturn(newCount)
			}
			.defaultIfEmpty(0) // Set default value if count is empty
			.flatMap { currentCount ->
				if (currentCount > 0) {
					Mono.just(currentCount)
				} else {
					dataRedisRepository.save(keyword, 1).thenReturn(1)
				}
			}
			.onErrorResume { Mono.error(it) }
			.subscribe { logger.info("--------> count : {}", it) }



		return mergeValues
			.subscribeOn(Schedulers.single())
			.flatMap { titles ->
				val titleObjects = titles.map { it?.let { it1 -> SearchResult.Title(it1) } }
				Mono.just(SearchResult(titleObjects))
			}
	}

	suspend fun mergeValuesFromWebClient(
		kakaoList: Mono<List<KakaoResponse.Document>?>,
		naverList: Mono<List<NaverResponse.Item>?>
	)
		: Mono<Set<String?>> {

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

		return Mono.zip(commonList, kakaos, navers)
			.map { it.t1.plus(it.t2).plus(it.t3) }
			.flatMap { Mono.just(it) }
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it) }
	}

	suspend fun getAllValues(): Flux<Any> {
		return dataRedisRepository.getAll()
	}


	suspend fun saveKeyValue(keyword: String, value: Int): Mono<Boolean> {
		return dataRedisRepository.save(keyword, value)
	}


	suspend fun getKeywordCount(keyword: String): Mono<Any> {

		dataRedisRepository.get(keyword)
			.subscribe { logger.info("--------> count : {}", it) }

		return dataRedisRepository.get(keyword)
	}

}

