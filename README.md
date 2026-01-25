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
Create and start the containers:
```bash
docker compose up -d
```

## Install with Kubernetes
Install all objects with Kustomize:
```bash
kubectl kube apply -k kustomize/base
```

Delete the project namespace and the objects if not needed anymore:
```bash
kubectl delete namespace notes
```
