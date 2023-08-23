package com.mytest.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


//@SpringBootTest
@SpringBootApplication
class TestApplicationTests {

	fun main(args: Array<String>) {
		SpringApplication.run(TestApplicationTests::class.java, *args)
	}

}
