plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.detekt)
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
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.config.yaml)

    implementation(libs.logback.classic)

    implementation(libs.mysql)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)

    implementation(libs.koin)

    // test libraries
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)

    testImplementation(libs.kotlin.test.junit)

    testImplementation(libs.strikt)

    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.junit.jupiter)
}
