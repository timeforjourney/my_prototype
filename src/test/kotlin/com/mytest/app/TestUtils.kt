package com.mytest.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

class TestUtils<T> {


	companion object {
		fun <T> convertString(jsonString: T): String? {
			val mapper = ObjectMapper()
			mapper.registerModule(JavaTimeModule())
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			return mapper.writeValueAsString(jsonString)
		}
	}
}