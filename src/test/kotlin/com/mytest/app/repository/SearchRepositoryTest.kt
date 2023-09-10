package com.mytest.app.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.notNull
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


//@WebFluxTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringJUnitConfig(CustomWebClientConfig::class)
//@ExtendWith(SpringExtension::class)
//@ExtendWith(MockitoExtension::class)
internal class SearchRepositoryTest
{

//	@Autowired
	lateinit var client: WebTestClient


//	@Autowired
	private lateinit var webTestClient: WebTestClient

//	@MockBean
//	private val searchRepository : SearchRepository = mock(SearchRepository::class.java)

	private lateinit var searchRepository: SearchRepository


	@BeforeEach
	fun setUp() {
//		client = WebTestClient.bindToApplicationContext(context).build()
//		client = WebTestClient.bindToServer()
//			.baseUrl("http://localhost:8080")
//			.build()

		searchRepository = mock(SearchRepository::class.java)
	}


	val keyword = "cafe"
	val page = "1"
	val size = "10"


	@Test
	internal suspend fun contextLoads() {

		given(searchRepository.getKakaoSearchResult(keyword, page, size)).willReturn(null)
//		when(searchRepository.getKakaoSearchResult(keyword, page, size)) {
//			-> println("null")
//			else -> println("not null")
//		}
//		assertThat(searchRepository.getKakaoSearchResult(keyword, page, size )).isNotNull
	}

//	@BeforeEach
//	fun setUp() {
//		webTestClient = WebTestClient
//			.bindToServer()
////			.baseUrl("http://localhost:8080")
//			.build()
//	}

//	@Test
//	@Order(1)
	fun testCreateGithubRepository() {
//		val repoRequest = RepoRequest("test-webclient-repository", "Repository created for testing WebClient")
		client.post().uri("/api/repos")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
//			.body(Mono.just<Any>(repoRequest), RepoRequest::class.java)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.name").isNotEmpty()
			.jsonPath("$.name").isEqualTo("test-webclient-repository")
	}

	@Test
//	@Order(1)
	fun callApiTest(){
		client.get()
			.uri("/test")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
	}


	@Test
	suspend fun testGetKakaoSearchResult() {


//		searchRepository.getKakaoSearchResult(keyword, page, size)


		// 테스트에서 result를 검증하는 코드 작성
	}
}