import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.mytest.hungry"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("io.projectreactor.addons:reactor-extra")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.+")

//	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
//	implementation("org.jetbrains.kotlin:kotlin-stdlib")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("org.springframework.boot:spring-boot-starter-validation")
//	implementation("org.jetbrains.kotlin:kotlin-test-junit5")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
//	testImplementation("io.projectreactor:reactor-test")

//	testCompileOnly("org.junit.jupiter:junit-jupiter-api:5.3.1")
//	testCompileOnly("org.junit.jupiter:junit-jupiter-params:5.3.1")
//	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

	compileOnly("io.projectreactor:reactor-tools:")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.bootJar {
	mainClass.set("com.mytest.app.TestApplication")
}