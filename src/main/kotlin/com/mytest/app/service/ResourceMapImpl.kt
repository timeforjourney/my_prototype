package com.mytest.app.service

import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import reactor.core.publisher.Mono

class ResourceMapImpl : ResourceMapClient{
	override fun getCommonList(
		kakaoList: Mono<List<KakaoResponse.Document>>,
		naverList: Mono<List<NaverResponse.Item>>
	): List<String> {
		TODO("Not yet implemented")

//		val executeStrategy: ReportExecuteStrategy =
//			reportExecutorFactory.findExecuteStrategyByType(createRequest.getType())
	}

	override fun getKakaoList(kakaoList: Mono<List<KakaoResponse.Document>>, commonList: Set<String>): List<String> {
		TODO("Not yet implemented")
	}

	override fun getNaverList(naverList: Mono<List<NaverResponse.Item>>, commonList: Set<String>): List<String> {
		TODO("Not yet implemented")
	}
}