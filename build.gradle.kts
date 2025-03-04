plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.4"
	jacoco
	id("org.sonarqube") version "5.1.0.4882"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.openapi.generator") version "7.5.0"
}

group = "it.gov.pagopa"
version = "0.0.1"

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val springdocOpenApiVersion = "2.7.0"
val janinoVersion = "3.1.12"
val openApiToolsVersion = "0.2.6"
val wiremockVersion = "3.10.0"
val javaJwtVersion = "4.4.0"
val jwksRsaVersion = "0.22.1"
val mapstructVersion = "1.5.5.Final"
val commonsIoVersion = "2.16.1"
val micrometerVersion = "1.3.5"
val springValidationVersion = "3.4.2"
val feignVersion = "4.2.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation:$springValidationVersion")
	implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenApiVersion")
	implementation("org.codehaus.janino:janino:$janinoVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:$feignVersion")
	implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
	// Spring Security
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-client
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	//lombok
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")



	/**
	* Mapstruct
	* https://mapstruct.org/
	* mapstruct dependencies must always be placed after the lombok dependency
 	* or the generated mappers will return an empty object
	**/
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

	// validation token jwt
	implementation("com.auth0:java-jwt:$javaJwtVersion")
	implementation("com.auth0:jwks-rsa:$jwksRsaVersion")

	// Forced transient dependecies to solve CVEs
	implementation ("commons-io:commons-io:$commonsIoVersion")

	//	Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.mockito:mockito-core")
	testImplementation ("org.wiremock:wiremock-standalone:$wiremockVersion")
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
	mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}
tasks {
	test {
		jvmArgs("-javaagent:${mockitoAgent.asPath}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

val projectInfo = mapOf(
		"artifactId" to project.name,
		"version" to project.version
)

tasks {
	val processResources by getting(ProcessResources::class) {
		filesMatching("**/application.yml") {
			expand(projectInfo)
		}
	}
}

configurations {
	compileClasspath {
		resolutionStrategy.activateDependencyLocking()
	}
}

tasks.compileJava {
	dependsOn("openApiGenerate")
}

configure<SourceSetContainer> {
	named("main") {
		java.srcDir("$projectDir/build/generated/src/main/java")
	}
}

springBoot {
	mainClass.value("it.gov.pagopa.arc.PagopaArcBeApplication")
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$rootDir/openapi/pagopa-arc-be.openapi.yaml")
	outputDir.set("$projectDir/build/generated")
	apiPackage.set("it.gov.pagopa.arc.controller.generated")
	modelPackage.set("it.gov.pagopa.arc.model.generated")
	configOptions.set(mapOf(
			"dateLibrary" to "java8",
			"requestMappingMode" to "api_interface",
			"useSpringBoot3" to "true",
			"interfaceOnly" to "true",
			"useTags" to "true",
			"generateConstructorWithAllArgs" to "false",
			"generatedConstructorWithRequiredArgs" to "false",
			"additionalModelTypeAnnotations" to "@lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.RequiredArgsConstructor"
	))
	typeMappings.set(mapOf(
			"DateTime" to "java.time.LocalDateTime",
			"zoned-date-time" to "java.time.ZonedDateTime"
	))
}
