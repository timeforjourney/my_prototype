package com.mytest.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync

@EnableCaching
@EnableAsync
@SpringBootApplication
class TestApplication

fun main(args: Array<String>) {

	runApplication<TestApplication>(*args)
}
