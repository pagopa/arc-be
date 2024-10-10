plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	jacoco
	id("org.sonarqube") version "5.0.0.4638"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.openapi.generator") version "7.5.0"
}

group = "it.gov.pagopa"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val springdocOpenApiVersion = "2.5.0"
val janinoVersion = "3.1.12"
val openApiToolsVersion = "0.2.6"
val wiremockVersion = "3.5.4"
val javaJwtVersion = "4.4.0"
val jwksRsaVersion = "0.22.1"
val mapstructVersion = "1.5.5.Final"
val commonsIo = "2.16.1"
val tomcat = "10.1.25"

dependencies {
	implementation ("org.apache.tomcat.embed:tomcat-embed-core:$tomcat")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenApiVersion")
	implementation("org.codehaus.janino:janino:$janinoVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation ("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
	implementation ("commons-io:commons-io:$commonsIo")
	// Spring Security
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-client
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	//lombok
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")
	// CVE Fix



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

	//	Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.mockito:mockito-core")
	testImplementation ("org.wiremock:wiremock-standalone:$wiremockVersion")
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
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
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