package com.mytest.app

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync
import java.util.*


@EnableCaching
@EnableAsync
@SpringBootApplication
class SearchRankApplication

fun main(args: Array<String>) {
	runApplication<SearchRankApplication>(*args)
}

@PostConstruct
fun started() {
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}
