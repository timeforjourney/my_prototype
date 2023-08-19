package com.mytest.app.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Repository
class KakaoSearchRepository(@Qualifier("customWebClient") private val webClient: WebClient) {

    fun getKakaoSearchResult() : Mono<String> {

        val webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
            .build()


        val setupHeaderWebClient = webClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK 4b57f0e1f8d4b81a20b9fe45b7fdd682")
            .build()

        return setupHeaderWebClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("query", "카카오프렌즈")
                    .queryParam("page", "1")
                    .queryParam("size", "10")
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String::class.java)
//            .subscribe()
            .onErrorResume { Mono.error(it) }


//        val c = setupHeaderWebClient.mutate()
//            .defaultHeader(HttpHeaders.AUTHORIZATION, token)
//            .build()

    }


}