package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import reactor.core.publisher.Mono

interface ResourceMapClient {


	fun getCommonList(kakaoList: Mono<List<KakaoResponse.Document>> , naverList: Mono<List<NaverResponse.Item>>): List<String>

	fun getKakaoList(kakaoList: Mono<List<KakaoResponse.Document>>, commonList : Set<String> ): List<String>

	fun getNaverList(naverList: Mono<List<NaverResponse.Item>>, commonList : Set<String> ): List<String>
}