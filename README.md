## Commands for local testing
1. Change directory to app
```
cd app
```

2. Software Composition Analysis (SonaType)
```
./mvnw org.sonatype.ossindex.maven:ossindex-maven-plugin:audit   
```

2. Lint (Spotless)

- Check linting issues
```
./mvnw spotless:check
```
- Auto-Correct linting issues
```
./mvnw spotless:apply
```

3. JaCoCo and Unit Test

JaCoCo report in **target/site/jacoco**
```
./mvnw clean verify
```

## Docker

The following will run on our **Centralised Test Database** hosted on DigitalOcean

1. Build docker image
```
docker build -f ../docker/Dockerfile -t kakigowhere-springboot .
```

2. Run container
```
docker run -d \
  --name backend \
  -p 8080:8080 \
  -e CSV_DIR=/uploads/csv/ \
  -v docker_app_csv:/uploads/csv \
  kakigowhere-springboot

```

3. Persist places.csv data in docker volume by calling http://localhost:8080/api/places/ml/export


4. (Optional) Check contents (first 20 rows)
```
docker run --rm -v docker_app_csv:/from alpine sh -c "head -n 20 /from/places.csv"
```

5. Stop and remove container
```
docker rm -f backend
```

## Docker Compose

The following will run on your **OWN** local database container

1. Build and run Springboot + MySQL containers
```
docker compose -f ../docker/docker-compose.dev.yml up -d --build
```

Add and access local database on your workbench using below:

| Setting   | Value       |
|-----------|-------------|
| Hostname  | `127.0.0.1` |
| Port      | `3305`      |
| Username  | `root`      |
| Password  | `pw`        |

2. Stop containers
```
docker compose -f ../docker/docker-compose.dev.yml down
```

Data will persist when you restart containers due to docker volume