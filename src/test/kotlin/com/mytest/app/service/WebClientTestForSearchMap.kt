package com.mytest.app.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.*


//@SpringJUnitConfig(WebConfig::class)
//@RunWith(SpringRunner::class)
//@ExtendWith()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebClientTestForSearchMap {

    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp(context: ApplicationContext) {
//        client = WebTestClient.bindToApplicationContext(context).build()
        client = WebTestClient.bindToServer()
                .baseUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                .build()


//        val httpClient: HttpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                .responseTimeout(Duration.ofMillis(5000))
//                .doOnConnected { conn ->
//                    conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//                            .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//                }
//
//        val client = WebClient.builder()
//                .clientConnector(ReactorClientHttpConnector(httpClient))
//                .build()
    }

    @Test
    fun getSearchMapResult() {


        val function: RouterFunction<*> = RouterFunctions.route(
                RequestPredicates.GET("/resource")
        ) {
            request: ServerRequest? -> ServerResponse.ok().build()
        }

        WebTestClient
                .bindToRouterFunction(function)
                .build().get().uri("/resource")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()


//        client.get().uri("https://dapi.kakao.com/v2/local/search/keyword.json")
//                .header("Authorization",  "KakaoAK 4b57f0e1f8d4b81a20b9fe45b7fdd682")
////                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//        WebTestClient.
    }
}