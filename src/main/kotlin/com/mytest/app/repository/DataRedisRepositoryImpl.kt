package com.mytest.app.repository

import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Repository
class DataRedisRepositoryImpl(val redisOperations: ReactiveRedisOperations<String, Any>)
	: DataRedisRepository {


	override fun save(key: String, value: Int): Mono<Boolean> {
		return redisOperations.opsForValue().set(key, value)
	}

	override suspend fun get(key: String): Mono<Any> {
		return redisOperations.opsForValue().get(key)
			.subscribeOn(Schedulers.parallel())
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it)  }
	}

	override suspend fun getAll(): Flux<Any> {

		return redisOperations.keys("*")
			.flatMap { redisOperations.opsForValue().get(it) }
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it)}
			.cast(Any::class.java)
	}

}