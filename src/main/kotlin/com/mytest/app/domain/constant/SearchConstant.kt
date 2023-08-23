package com.mytest.app.domain.constant

enum class SearchConstant (val param: String)  {

	KAKAO_SEARCH_PAGE("page"),
	KAKAO_SEARCH_PAGE_SIZE("size"),
	NAVER_SEARCH_PAGE("start"),
	NAVER_SEARCH_PAGE_SIZE("display"),
	NAVER_SEARCH_HOST("Host"),
	NAVER_SEARCH_CLIENTID("X-Naver-Client-Id"),
	NAVER_SEARCH_CLIENTSECRET("X-Naver-Client-Secret"),
	SEARCH_QUERY("query"),
	SEARCH_RANK("searchRank"),

}