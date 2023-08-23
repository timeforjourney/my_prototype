package com.mytest.app.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DataRedisRepository {

	fun save(key: String, value: Int) : Mono<Boolean>
	suspend fun get(key : String) : Mono<Any>
	suspend fun getAll() : Flux<Any>

}