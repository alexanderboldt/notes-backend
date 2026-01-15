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

### CI/CD
- GitHub Actions

## Install with Docker
1. Build the ktor docker image:
```bash
./gradlew clean publishImageToLocalRegistry
```
2. Create and start the containers:
```bash
docker compose up -d
```

## Install with Kubernetes (microk8s)
Build the ktor docker image:
```bash
./gradlew clean publishImageToLocalRegistry
```

Push the image from docker to kubernetes:
```bash
docker push localhost:32000/notes/app:1.0.0
```

Create the project namespace:
```bash
microk8s kubectl apply -f k8s/notes-namespace.yml
```

Create all remaining objects from the folder:
```bash
microk8s kubectl apply -f k8s/
```

Delete the project namespace and the objects if not needed anymore:
```bash
microk8s kubectl delete namespace notes
```
