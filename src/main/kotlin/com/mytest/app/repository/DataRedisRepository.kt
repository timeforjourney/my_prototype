package com.mytest.app.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DataRedisRepository {

	fun save(key: String, value: Int) : Mono<Boolean>
	suspend fun get(key : String) : Mono<Any>
	suspend fun getAll() : Flux<Any>

	fun getZSetScoreByValue(rankKey: String, keyword: String): Mono<Double>

	fun incrementZSetKeyScore(rankKey: String, value: String): Mono<Double>

	fun addZSetKeyScore(rankKey: String, value: String, score: Double): Mono<Boolean>

	suspend fun rangeZSetScores(key: String): Flux<Any>
}