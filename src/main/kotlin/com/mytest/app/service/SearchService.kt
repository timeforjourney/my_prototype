package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SearchService  {

//	suspend fun searchKakaoResult(keyword: String, page: String, size: String): Mono<KakaoResponse>

//	suspend fun searchNaverResult(keyword: String, page: String, size: String): Mono<NaverResponse>

	suspend fun sortSearchResult(keyword: String, rank: String, page: String, size: String): Mono<SearchResult>

//	suspend fun getAllValues(): Flux<Any>

//	suspend fun saveKeyValue(keyword: String, value: Int): Mono<Boolean>

//	suspend fun getScoreByValue(rank: String, keyword: String): Mono<Double>

	suspend fun getZsetValues(keyword: String): Flux<Any>
}