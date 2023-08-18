package com.mytest.app.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class SearchController {

//    @CacheableMono
//    @GetMapping("/hello")
//    fun hello(): Mono<String> {
//        return Mono.just("Hello, WebFlux!")
//    }

}