package com.mytest.app.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.*
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder


@Configuration
@EnableRedisRepositories
@EnableCaching
class RedisCacheConfig {

	private val logger = LoggerFactory.getLogger(this::class.java)


	@Value("\${spring.data.redis.host}")
	lateinit var redisHost: String

	@Value("\${spring.data.redis.port}")
	private val redisPort: Int? = 6379


	@Value("\${spring.data.redis.database}")
	private val database: Int? = 0



	@Bean
	fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {

		logger.info("lettuce factory  - host : {} , port :  {}", redisHost, redisPort)

		val connectionFactory = LettuceConnectionFactory(RedisStandaloneConfiguration( redisHost,redisPort!!))
//		connectionFactory.database = database!!
		connectionFactory.afterPropertiesSet()
//		connectionFactory.validateConnection()

		return connectionFactory
	}


//	@Bean
//	fun redisOperations(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory?): ReactiveRedisOperations<String, Any>
//	{
//		val serializer: Jackson2JsonRedisSerializer<Any?> = Jackson2JsonRedisSerializer(Any::class.java)
//
//		val builder: RedisSerializationContextBuilder<String, Any> =
//			RedisSerializationContext.newSerializationContext<String, Any>(StringRedisSerializer())
//
//		val context: RedisSerializationContext<String, Any> = builder
//			.value(serializer)
//			.build()
//		return ReactiveRedisTemplate(reactiveRedisConnectionFactory!!, context)
//	}


	@Bean
	fun reactiveRedisTemplate(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory?)
		: ReactiveRedisTemplate<String, Any> {

		val serializationContext = RedisSerializationContext
			.newSerializationContext<String, Any>(StringRedisSerializer())
			.key(StringRedisSerializer())
			.value(GenericToStringSerializer(Any::class.java))
			.hashKey(StringRedisSerializer())
			.hashValue(GenericJackson2JsonRedisSerializer())
			.build()

		return ReactiveRedisTemplate(reactiveRedisConnectionFactory!!, serializationContext)
	}

}