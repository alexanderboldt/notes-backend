# Notes-Backend

This is a backend application for managing notes.

## Tech-Stack

### Development
- Kotlin
- Ktor
- Kotlinx-Serialization
- Exposed
- Koin
- MySql

### Test
- Kotest
- Docker-Testcontainer

### Buildsystem
- Gradle

### Code Analysis
- Detekt

## Install
1. Build the ktor docker image:
```bash
./gradlew clean publishImageToLocalRegistry
```
2. Create and start the containers:
```bash
docker compose up -d
```
