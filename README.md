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
1. Build the ktor docker image:
```bash
./gradlew clean publishImageToLocalRegistry
```

2. Push the image from docker to kubernetes:
```bash
docker push localhost:32000/notes/app:latest
```

3. Apply statefulset and service of mysql:
```bash
microk8s kubectl apply -f k8s/mysql-statefulset.yml
microk8s kubectl apply -f k8s/mysql-service.yml
```

4. Apply deployment and service of notes:
```bash
microk8s kubectl apply -f k8s/notes-deployment.yml
microk8s kubectl apply -f k8s/notes-service.yml
```

5. Scale down the pods if temporary not needed:
```bash
microk8s kubectl scale statefulset mysql --replicas=0
microk8s kubectl scale deployment notes --replicas=0
```

6. Delete the pods:
```bash
microk8s kubectl delete statefulset mysql
microk8s kubectl delete deployment notes
```
