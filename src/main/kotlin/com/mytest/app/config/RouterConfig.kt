package com.mytest.app.config

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router
import java.time.Duration


@Configuration
class RouterConfig {

    @Bean
    fun route() = router {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/hello") { ok().bodyValue("Hello, WebFlux!") }
            }
        }
    }


}