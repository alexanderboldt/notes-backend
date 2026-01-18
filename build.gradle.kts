plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.detekt)
}

group = "org.alex.notes"

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

tasks.withType<Test> {
    useJUnitPlatform()
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

    testImplementation(libs.kotest.runner.junit)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.ktor)

    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.junit.jupiter)
}

ktor {
    docker {
        jreVersion = JavaVersion.VERSION_21
        localImageName = "notes/app"
        imageTag = "2.0.0"
    }
}

jib {
    from {
        image = "eclipse-temurin:21-jdk-alpine"
    }
    to {
        image = "ghcr.io/alexanderboldt/notes/app"
        tags = setOf("2.1.0", "latest")
        auth {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    container {
        ports = listOf("4000")
        mainClass = "org.alex.notes.ApplicationKt"
    }
}
