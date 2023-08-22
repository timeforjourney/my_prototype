package com.mytest.app.controller

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import com.mytest.app.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class SearchController(private val searchService: SearchService) {


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/kakao")
	fun kakaoMap(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page" , required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String,
	): Mono<KakaoResponse> {
		return searchService.searchKakaoResult(keyword, page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/naver")
	fun naverMap(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page", required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String
	): Mono<NaverResponse> {
		return searchService.searchNaverResult(keyword, page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/result")
	fun result(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page", required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String
	) : Mono<SearchResult> {
		return searchService.sortSearchResult(keyword, page, size)
	}
}