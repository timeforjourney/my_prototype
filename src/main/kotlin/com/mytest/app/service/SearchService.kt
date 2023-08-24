package com.mytest.app.service

import com.mytest.app.domain.SearchResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SearchService  {

	suspend fun sortSearchResult(keyword: String, rank: String, page: String, size: String): Mono<SearchResult>
	suspend fun getZsetValues(keyword: String): Flux<Any>
}