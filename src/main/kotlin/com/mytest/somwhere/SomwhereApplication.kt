package com.mytest.somwhere

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SomwhereApplication

fun main(args: Array<String>) {
	runApplication<SomwhereApplication>(*args)
}
