package com.mytest.app.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoResponse(

	@JsonProperty("documents")
	val documents : List<Document>?
)
{

	@JsonIgnoreProperties(ignoreUnknown = true)
	data class Document(

		@JsonProperty("place_name")
		val placeName : String
	)
}