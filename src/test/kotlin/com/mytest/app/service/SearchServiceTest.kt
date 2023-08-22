package com.mytest.app.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.mytest.app.repository.SearchRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Mono


//@ExtendWith(MockitoExtension::class)
@ExtendWith(SpringExtension::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebFluxTest
//@Import(SearchRepository::class)
@AutoConfigureWebFlux
class SearchServiceTest (
   private val searchRepository: SearchRepository
//   val objectMapper : ObjectMapper
)  {

    val keyword = "판교노래방"
    val page = "1"
    val size = "10"


    @Test
    fun sortSearchResult() {

        val kakaoList = searchRepository.getKakaoSearchResult(keyword, page, size)
           .map { it.documents }

        val mapper = ObjectMapper().registerModule(JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val result = mapper.writeValueAsString(kakaoList)

//        val result = objectMapper.writeValueAsString(kakaoList)
        print("kakaoList : $result")
    }

}