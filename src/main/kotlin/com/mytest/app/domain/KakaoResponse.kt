package com.mytest.app.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


//@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoResponse(

	val documents : List<Document>
)
{
	data class Document(
		val place_name : String?,
//		val address_name : String?,
//		val road_address_name : String?,
//		val x : String?,
//		val y : String?,
	)
}