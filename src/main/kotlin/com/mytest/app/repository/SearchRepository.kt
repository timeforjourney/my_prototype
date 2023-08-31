package com.mytest.app.repository

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import reactor.core.publisher.Mono


interface SearchRepository {

	suspend fun getKakaoSearchResult(keyword: String, page: String, size: String): Mono<KakaoResponse>

	suspend fun getNaverSearchResult(keyword: String, page: String, size: String): Mono<NaverResponse>
}