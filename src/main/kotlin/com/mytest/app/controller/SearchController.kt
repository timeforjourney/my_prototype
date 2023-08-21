package com.mytest.app.controller

import com.mytest.app.domain.SearchResult
import com.mytest.app.service.SearchService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class SearchController(private val searchService: SearchService) {

	private val logger = LoggerFactory.getLogger(this::class.java)


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/result")
	suspend fun result(
		@RequestParam("keyword") keyword: String,
		@RequestParam("rank") rank: String,
		@RequestParam("page", required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String
	) : Mono<SearchResult> {

		return searchService.sortSearchResult(keyword, rank , page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/zsetScores")
	suspend fun zsetScores(
		@RequestParam("key") keyword: String
	) : Flux<Any> {
		return  searchService.getZsetValues(keyword)
	}
}