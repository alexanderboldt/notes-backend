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
Build the ktor Docker image:
```bash
./gradlew clean publishImageToLocalRegistry
```

Create a tag for local Kubernetes and push it to the registry:
```bash
docker tag notes/app:2.0.0 localhost:32000/notes/app:2.0.0
docker push localhost:32000/notes/app:2.0.0
```

Create and start the containers:
```bash
docker compose up -d
```

## Install with Kubernetes (microk8s)
Install all objects with Kustomize:
```bash
microk8s kubectl kube apply -k kustomize/base
```

Copy the dashboards and provisioning files in the grafana pod:
```bash
microk8s kubectl cp grafana/dashboards <grafana-pod>:/var/lib/grafana
microk8s kubectl cp grafana/provisioning <grafana-pod>:/etc/grafana
```

Restart the grafana pod:
```bash
microk8s kubectl rollout restart deployment grafana
```

Delete the project namespace and the objects if not needed anymore:
```bash
microk8s kubectl delete namespace notes
```
