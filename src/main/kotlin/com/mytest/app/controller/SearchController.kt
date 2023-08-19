package com.mytest.app.controller

import com.mytest.app.service.SearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class SearchController (private val searchService: SearchService){

    @GetMapping("/kakao")
    fun hello(): Mono<String> {
        return searchService.searchKakaoResult()
    }

}