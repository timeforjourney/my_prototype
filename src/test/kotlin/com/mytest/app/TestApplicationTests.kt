package com.mytest.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


//@SpringBootTest
//@SpringBootApplication
class TestApplicationTests {

	fun main(args: Array<String>) {
		SpringApplication.run(TestApplicationTests::class.java, *args)
	}



}
