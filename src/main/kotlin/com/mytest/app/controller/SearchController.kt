package com.mytest.app.controller

import com.mytest.app.controller.request.SaveKeyword
import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.domain.SearchResult
import com.mytest.app.service.SearchService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URLEncoder

@RestController
@RequestMapping("/api")
class SearchController(private val searchService: SearchService) {

	private val logger = LoggerFactory.getLogger(this::class.java)


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/kakao")
	suspend fun kakaoMap(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page" , required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String,
	): Mono<KakaoResponse> {
		return searchService.searchKakaoResult(keyword, page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/naver")
	suspend fun naverMap(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page", required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String
	): Mono<NaverResponse> {
		return searchService.searchNaverResult(keyword, page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/result")
	suspend fun result(
		@RequestParam("keyword") keyword: String,
		@RequestParam("page", required = false, defaultValue = "1") page: String,
		@RequestParam("size", required = false, defaultValue = "10") size: String
	) : Mono<SearchResult> {

//		val keywordParam = URLEncoder.encode(keyword, "UTF-8")
		return searchService.sortSearchResult(keyword, page, size)
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/allKeys")
	suspend fun allKeys() : Flux<Any> {
		return searchService.getAllValues()
	}


	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/save")
	suspend fun save(
		@RequestBody @Valid request : SaveKeyword
	) : Mono<Boolean> {

		val keyword = URLEncoder.encode(request.keyword, "UTF-8")
		return searchService.saveKeyValue(keyword, request.count)
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/keywordCount")
	suspend fun getKeywordCount(
		@RequestParam("keyword") keyword: String
	) : Mono<Int> {


		return  searchService.getKeywordCount(keyword)
			.map { it.toString().toIntOrNull() ?: 0 }
	}
}