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
docker push localhost:32000/notes/app:latest
```

Create the project namespace:
```bash
microk8s kubectl apply -f k8s/notes-namespace.yml
```

Create ConfigMap, Statefulset and Service of mysql:
```bash
microk8s kubectl apply -f k8s/mysql-configmap.yml
microk8s kubectl apply -f k8s/mysql-statefulset.yml
microk8s kubectl apply -f k8s/mysql-service.yml
```

Apply Deployment and Service of the notes app:
```bash
microk8s kubectl apply -f k8s/notes-deployment.yml
microk8s kubectl apply -f k8s/notes-service.yml
```

Scale down the pods if temporary not needed:
```bash
microk8s kubectl scale statefulset mysql --replicas=0
microk8s kubectl scale deployment notes --replicas=0
```

Delete the pods:
```bash
microk8s kubectl delete statefulset mysql
microk8s kubectl delete deployment notes
```

Delete the project namespace if not needed anymore:
```bash
microk8s kubectl delete namespace notes
```
