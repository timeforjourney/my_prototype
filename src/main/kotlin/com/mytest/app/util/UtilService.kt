package com.mytest.app.util

import org.springframework.stereotype.Component

@Component
class UtilService {

	companion object {
		// 정규 표현식을 사용하여 HTML 태그 제거
		fun removeHtmlTags(input: String): String {
			return input.replace(Regex("<[^>]*>"), "")
		}
	}
}