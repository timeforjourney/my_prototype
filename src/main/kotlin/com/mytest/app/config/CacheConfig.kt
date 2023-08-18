package com.mytest.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
class CacheConfig {


    @Bean
    fun cacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair

                        .fromSerializer<Any>(GenericJackson2JsonRedisSerializer()))
    }

    @Bean
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory?,
                          objectMapper: ObjectMapper?): RedisCacheManager
    {
        val configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(60))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer<String>(StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer<Any>(GenericJackson2JsonRedisSerializer(objectMapper!!)))

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory!!)
                .cacheDefaults(configuration)
                .build()
    }


    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
        return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManager.RedisCacheManagerBuilder ->
            builder
                    .withCacheConfiguration("itemCache",
                            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                    .withCacheConfiguration("customerCache",
                            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
        }
    }
}