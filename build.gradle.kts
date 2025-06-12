plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)

    id("io.gitlab.arturbosch.detekt") version("1.23.8")
}

group = "org.alex.notes"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

detekt {
    config.setFrom("detekt.yml")
    buildUponDefaultConfig = true
}

repositories {
    mavenCentral()
}

dependencies {
    // development libraries
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback.classic)
    implementation(libs.mysql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.koin)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.call.logging)

    // test libraries
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.strikt)
    testImplementation(libs.testcontainers.junit.jupiter)
}
