
http Request spec file 
---

    1. intellij 파일 : searchRequest.http
    2. postman : /resources/postman 경로
    예시) 
    지도 검색 - GET http://localhost:8080/api/result?keyword=판교곱창&rank=myRank
    검색 순위 - GET http://localhost:8080/api/zsetScores?key=myRank

---

적용한 추가 비기본 라이브러리 목록 : 
--
    //Redis 사용 라이브러리 
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    //webflux 라이브러리 : netty 사용
    implementation("org.springframework.boot:spring-boot-starter-webflux")


    //kotlin data class 직렬화/역직렬화 
    com.fasterxml.jackson.module:jackson-module-kotlin



저장소 : Redis
---

    (base) > $ docker pull redis:7.2.0-alpine3.18
    (base) > $ docker-compose redis-docker-compose.yaml up -d

아래 redis 용 docker-compose 파일을 작성하여 실행한다. 

redis-docker-compose.yaml



         version: '7.2'
            services:
                redis:
                    image: redis:7.2.0-alpine3.18
                    command: redis-server --port 6379
                    container_name: redis_standalone
                    hostname: redis_standalone
                    labels:
                    - "name=redis"
                      - "mode=standalone"
                      ports:
                      - 6379:6379

sorted set 을 사용하여 검색순서를 카운트 
-> 키워드 목록 제공 
--- 