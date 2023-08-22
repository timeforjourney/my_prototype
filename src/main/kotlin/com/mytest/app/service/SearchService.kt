package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
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


	fun sortSearchResult(keyword: String, page: String, size : String): Mono<List<Int>> {

//		val kList = searchRepository.getKakaoSearchResult(keyword, page, size)
//		val nList = searchRepository.getNaverSearchResult(keyword, page, size)
//
//		Mono.zip(kList, nList)
//			.subscribeOn(Schedulers.parallel())
//			.map { it.t1.documents!!.map { it.placeName }
//				.intersect(it.t2.items!!.map { it.title }) }
//			.flatMap { Mono.just(it) }
//			.subscribe { it.forEach { logger.info(it.toString()) } }



		val kakaoList = searchRepository.getKakaoSearchResult(keyword, page, size)
				.map { it.documents }


		val naverList = searchRepository.getNaverSearchResult(keyword, page, size)
				.map { it.items }

		Mono.zip(kakaoList, naverList)
			.subscribeOn(Schedulers.parallel())
			.map { it.t1.map { it.placeName }
				.intersect(it.t2.map { it.title }) }
			.flatMap { Mono.just(it) }
			.subscribe { it.forEach { logger.info("--> sub 1 : {}",it.toString()) } }

		Mono.zip(kakaoList, naverList)
			.subscribeOn(Schedulers.parallel())
			.map{
				it.t1.map { it.placeName }
					.intersect(it.t2.map { it.title })
					.map { it1 -> it.t1.indexOfFirst { it.placeName == it1 } }
			}
			.subscribe{ it.forEach { logger.info("--> sub2 : {}",it.toString()) } }


		val commonMonoList: Mono<List<Int>> = Mono.zip(kakaoList, naverList)
			.subscribeOn(Schedulers.parallel())
			.map{
				it.t1.map { it.placeName }
					.intersect(it.t2.map { it.title })
					.map { it1 -> it.t1.indexOfFirst { it.placeName == it1 } }
			}
		return commonMonoList

//		 kakaoList.zipWith(naverList)
//			.subscribe({
//				it.t1.forEach { it1 ->
//					it.t2.forEach { it2 ->
//						if (it1.placeName == it2.title) {
//							result += it1.placeName + "\n"
//						}
//					}
//				}
//			}, { it.printStackTrace() }, {
//				logger.info(result)
//			})

//			.map { it.t1.plus(it.t2) }

//			.filter({ it.t1.isNotEmpty() && it.t2.isNotEmpty() })

//			.map { (it.t, it.t2) -> it.t1.filter(it.t1 in t2) }
//			.map { (it.t1, it.t2) -> it.t1.filter(it.t1 in t2) }
//				.map { it.t1.plus(it.t2) }
//				.map { it.sortedBy { it.title } }
//				.subscribe { it.forEach { log.info(it.title) } }
	}
}

