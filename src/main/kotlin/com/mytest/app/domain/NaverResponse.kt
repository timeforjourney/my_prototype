package com.mytest.app.domain

import com.mytest.app.util.UtilService

data class NaverResponse (

	val items : List<Item>?
)
{

	class Item(
		title : String?
	)
	{

		var title : String? = title?.let { UtilService.removeHtmlTags(it) }
	}
}