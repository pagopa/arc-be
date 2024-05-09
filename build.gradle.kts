plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	jacoco
	id("org.sonarqube") version "5.0.0.4638"
	id("com.github.ben-manes.versions") version "0.51.0"
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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenApiVersion")
	implementation("org.codehaus.janino:janino:$janinoVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//	Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.mockito:mockito-core")

}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
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
