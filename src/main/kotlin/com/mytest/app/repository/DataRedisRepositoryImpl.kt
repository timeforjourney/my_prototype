package com.mytest.app.repository

import com.mytest.app.domain.constant.CountConstant
import org.springframework.data.domain.Range
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Repository
class DataRedisRepositoryImpl(val redisOperations: ReactiveRedisOperations<String, Any>
) : DataRedisRepository {

	private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

	override fun save(key: String, value: Int): Mono<Boolean> {
		return redisOperations.opsForValue().set(key, value)
//			.awaitFirstOrDefault(Mono.just(false))
	}

	override suspend fun get(key: String): Mono<Any> {
		return redisOperations.opsForValue().get(key)
			.subscribeOn(Schedulers.parallel())
//			.awaitSingle()
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it)  }
	}

	override suspend fun getAll(): Flux<Any> {

		return redisOperations.keys("*")
			.flatMap { redisOperations.opsForValue().get(it)}
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it)}
			.cast(Any::class.java)
	}

	override fun addZSetKeyScore(rankKey: String, value: String, score: Double): Mono<Boolean> {

		return redisOperations.opsForZSet().add(rankKey, value, score)
			.onErrorResume { Mono.error(it) }
	}

	/**
	 * ZSet의 Score를 증가
	 */
	override fun incrementZSetKeyScore(rankKey: String, value: String): Mono<Double> {

		return redisOperations.opsForZSet()
			.incrementScore(rankKey, value, CountConstant.SEARCH_PLUS_COUNT.count.toDouble())
	}

	/**
	 * ZSet의 Score를 반환
	 */
	override fun getZSetScoreByValue(rankKey: String, keyword: String): Mono<Double> {

		return redisOperations.opsForZSet().score(rankKey, keyword)
	}

	/**
	 * ZSet의 Score를 기준으로 내림차순 정렬하여 반환
	 */
	override suspend fun rangeZSetScores(key: String): Flux<Any> {


		return redisOperations.opsForZSet()
			.reverseRangeWithScores(key, Range.unbounded<Long>())
			.switchIfEmpty(Mono.empty())
			.onErrorResume { Mono.error(it) }
			.cast(Any::class.java)
	}

}