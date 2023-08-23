package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import com.mytest.app.domain.constant.CountConstant
import com.mytest.app.repository.DataRedisRepository
import com.mytest.app.repository.SearchRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Service
class SearchServiceImpl(
	val searchRepository: SearchRepository,
	val dataRedisRepository: DataRedisRepository,
) : SearchService {

	private val logger = LoggerFactory.getLogger(this::class.java)


	private suspend fun fetchKakaoResults(keyword: String,page: String,size: String): Mono<List<KakaoResponse.Document>> {

		return searchRepository.getKakaoSearchResult(keyword, page, size)
			.subscribeOn(Schedulers.single())
			.map { it.documents }
	}

	private suspend fun fetchNaverResults(keyword: String, page: String, size: String): Mono<List<NaverResponse.Item>> {
		return searchRepository.getNaverSearchResult(keyword, page, size)
			.subscribeOn(Schedulers.single())
			.map { it.items }
	}


	/**
	 * 카카오, 네이버 검색 결과를 합쳐서 반환
	 */
	override suspend fun sortSearchResult(
		keyword: String,
		rank: String,
		page: String,
		size: String
	): Mono<SearchResult> {

		val kakaoList = fetchKakaoResults(keyword, page, size)
		val naverList = fetchNaverResults(keyword, page, size)

		Assert.notNull(kakaoList, "kakaoList must not be empty")
		Assert.notNull(naverList, "naverList must not be empty")

		val mergeValues = mergeValuesFromWebClient(kakaoList, naverList)

		updateRedisRanking(keyword, rank)
			.onErrorResume { Mono.error(it) }
			.subscribe { logger.debug("--------> result count : {}", it) }


		return mergeValues
			.subscribeOn(Schedulers.single())
			.flatMap { titles ->
				val titleObjects = titles.map { it?.let { it1 -> SearchResult.Title(it1) } }
				Mono.just(SearchResult(titleObjects))
			}
	}


	/**
	 * redis 에서 검색어 순위, 스코어를 업데이트
	 */
	private suspend fun updateRedisRanking(keyword: String, rank: String): Mono<Boolean> {

		return dataRedisRepository.getZSetScoreByValue(rank, keyword)
			.flatMap {
				dataRedisRepository.incrementZSetKeyScore(rank, keyword)
					.doOnSuccess { logger.info("increased keyword: {}, score : {}", keyword, it) }
					.thenReturn(true)
			}
			.defaultIfEmpty(false)
			.flatMap { incrementProcess ->
				if (incrementProcess == true) {
					Mono.just(true)
				} else {
					dataRedisRepository.addZSetKeyScore(rank, keyword, CountConstant.SEARCH_INIT_COUNT.count.toDouble())
						.doOnSuccess { logger.info("-> added new keyword: {}, score : {}", keyword, it) }
						.thenReturn(true)
				}
			}
	}


	/**
	 * 공통키워드, 카카오, 네이버순으로 정렬
	 */
	suspend fun mergeValuesFromWebClient(
		kakaoList: Mono<List<KakaoResponse.Document>>,
		naverList: Mono<List<NaverResponse.Item>>
	): Mono<Set<String?>> {

		//카카오, 네이버 검색 결과 중 공통 키워드
		val commonList = Mono.zip(kakaoList, naverList)
			.map {
				it.t1.map { it.placeName }.intersect(it.t2.map { it.title })
			}
			.flatMap { Mono.just(it) }

		//카카오 검색 결과 중 공통 키워드를 제외한 결과
		val kakaos = Mono.zip(kakaoList, commonList)
			.map {
				it.t1.map { it.placeName }.subtract(it.t2)
			}
			.flatMap { Mono.just(it) }

		//네이버 검색 결과 중 공통 키워드를 제외한 결과
		val navers = Mono.zip(naverList, commonList)
			.map {
				it.t1.map { it.title }.subtract(it.t2)
			}
			.flatMap { Mono.just(it) }

		return Mono.zip(commonList, kakaos, navers)
			.map { it.t1.plus(it.t2).plus(it.t3) }
			.flatMap { Mono.just(it) }
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it) }
	}


	override suspend fun getZsetValues(keyword: String): Flux<Any> {
		return dataRedisRepository.rangeZSetScores(keyword)
	}

}

