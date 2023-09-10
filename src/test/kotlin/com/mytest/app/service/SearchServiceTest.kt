package com.mytest.app.service

import com.mytest.app.TestUtils
import com.mytest.app.domain.KakaoResponse
import com.mytest.app.domain.NaverResponse
import com.mytest.app.repository.DataRedisRepository
import com.mytest.app.repository.SearchRepository
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.BDDAssumptions.given
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


//@WebFluxTest
//@Import(SearchRepository::class)
//@AutoConfigureWebFlux
//@AutoConfigureMockMvc
//@ExtendWith(SpringExtension::class)
//@ExtendWith(MockitoExtension::class)
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@WebFluxTest
@SpringBootTest
class SearchServiceTest @Autowired constructor(

	val searchService: SearchService,

	val searchServiceImpl: SearchServiceImpl,

) {

	private val log = LoggerFactory.getLogger(this::class.java)


	private val mainThreadSurrogate = newSingleThreadContext("thread")

//	@BeforeEach
//	fun setUp() {
//		Dispatchers.setMain(mainThreadSurrogate)
//	}
//
//
//	@AfterEach
//	fun tearDown() {
//		Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
//		mainThreadSurrogate.close()
//	}


	val keyword = "판교노래방"
	val page = "1"
	val size = "10"


	@Test
	fun sortSearchResult() = runBlocking {

		val rank = "random"

		val search = searchService.sortSearchResult(keyword, rank, page, size).block()

		val result = TestUtils.convertString(search)

		println("kakaoList : $result")

	}



	@Test
	fun mergeValuesFromWebClient() = runBlocking {


		val kakao1 = KakaoResponse.Document("카카오노래방")
		val kakao2 = KakaoResponse.Document("핑크노래방")
		val kakao3 = KakaoResponse.Document("필노래방")
		val kakao4 = KakaoResponse.Document("짱노래방")

		val kakaoList = listOf(kakao1, kakao2, kakao3, kakao4)


		val naver1 = NaverResponse.Item("카카오노래방")
		val naver2 = NaverResponse.Item("세븐스타 노래방")
		val naver3 = NaverResponse.Item("필노래방")
		val naver4 = NaverResponse.Item("달빛노래연습장")

		val naverList = listOf(naver1, naver2, naver3, naver4)

		val mergeValiues = searchServiceImpl.mergeValuesFromWebClient(Mono.just(kakaoList), Mono.just(naverList)).block()

		println("mergeValues : ${TestUtils.convertString(mergeValiues)}")

	}


	@Test
	fun updateRedisRanking() = runBlocking {

		val keyword = "판교노래방"
		val rank = "random"

		val result = searchServiceImpl.updateRedisRanking(keyword, rank).block()

		println("result : $result")

	}

	@Test
	fun test() {
		println("test")
	}

}