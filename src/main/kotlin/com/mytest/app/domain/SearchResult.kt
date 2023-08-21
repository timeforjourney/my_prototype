package com.mytest.app.domain

data class SearchResult(
	val place: List<Title?>
)
{
	data class Title(
		val title: String
	)
}
