package com.mytest.app.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mytest.app.util.UtilService

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverResponse (

	@JsonProperty("items")
	val items : List<Item>?
)
{

	@JsonIgnoreProperties(ignoreUnknown = true)
	class Item(

		@JsonProperty("title")
		title : String?
	)
	{

		var title : String? = title?.let { UtilService.removeHtmlTags(it) }
	}
}