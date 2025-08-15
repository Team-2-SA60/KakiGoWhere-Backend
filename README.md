## Production Server

KakiGoWhere is deployed on Digital Ocean, and will be kept running until 31st August 2025.

You can access our deployed Web Application [here](http://206.189.43.202/admin/login)

Access admin dashboard using
| Admin         |  Acount        |
|---------------|----------------|
| Email         | admin@kaki.com |
| Password      | admin          |

## 🛠️ Getting started using 🐳 Docker

Pre-requisite:
- Install **Docker Engine** [here](https://docs.docker.com/engine/install/)
- Install **MySQL Workbench**  (Optional but recommended) [here](https://dev.mysql.com/downloads/workbench/)
- Obtain **Google Places API key** (Needed for scheduled place updates) from [Google Cloud](https://developers.google.com/maps/documentation/places/web-service/overview)

---

1. Open terminal / command prompt

    ```
    mkdir KakiGoWhere
    ```

2. Change directory to KakiGoWhere

    ```
    cd KakiGoWhere
    ```

3. Clone repository

    ```
    git clone https://github.com/Team-2-SA60/KakiGoWhere-Backend.git
    ```

4. Change directory to KakiGoWhere-Backend

    ```
    cd KakiGoWhere-Backend/app
    ```

5. Open application.properties

    ```
        KakiGoWhere
        ├── KakiGoWhere-Backend
        │   ├── app
        │   │   ├── src
        │   │   │   ├── main
        │   │   │   │   ├── resources
        │   │   │   │   │   └── application.properties
    ```

6. (Optional) Input your obtained Google Place API key:

   > google.places.api.key=***PUT_YOUR_GOOGLE_PLACE_API_KEY_HERE***

---
**JUMP TO No Key section below** if you do **NOT** have a Google Place API key, certain features will not be available:
   - Add place using google search
   - Monthly scheduler to update place information)
---

7. Build and run SpringBoot + MySQL using Docker Compose

    ```
    docker compose -f ../docker/docker-compose.dev.yml up -d --build
    ```

8. Fully initialize database by manually triggering Places update (You must do **step 6** for this to work)

    ```
    curl --header "Content-Type: text/plain" --request POST --data "team2" http://localhost:8080/api/places/retrieve
    ```

9. Continue steps indicated on [KakiGoWhere-Frontend](https://github.com/Team-2-SA60/KakiGoWhere-Frontend) and [KakiGoWhere-ML](https://github.com/Team-2-SA60/KakiGoWhere-ML) to start the rest of the services


10. Finally you should have the following folder structure

    ```
        KakiGoWhere
        ├── KakiGoWhere-Backend
        ├── KakiGoWhere-Frontend
        ├── KakiGoWhere-ML
        ├── KakiGoWhere-Android
    ```
\
Assuming you have built all docker images, you can easily start up all containers by:

11. Changing directory to KakiGoWhere/KakiGoWhere-Backend/app

    ```
    cd KakiGoWhere/KakiGoWhere-Backend/app
    ```

12. Starting all containers

    ```
    docker compose -f ../docker/docker-compose.all.yml up -d --build
    ```

---

## 🔑❎ No Key: Without Google Place API key
1. Create docker volume

    ```
    docker volume create docker_app_image
    ```

2. Copy images into docker volume

    ```
    docker run --rm -v docker_app_images:/data -v ../uploads/images:/src busybox sh -c "cp -r /src/* /data/"
    ```

3. Build and run SpringBoot + MySQL using Docker Compose

    ```
    docker compose -f ../docker/docker-compose.dev.yml up -d --build
    ```

4. Open MySQL Workbench and access local database container using below:

| Setting   | Value       |
|-----------|-------------|
| Hostname  | `127.0.0.1` |
| Port      | `3305`      |
| Username  | `root`      |
| Password  | `pw`        |

5. Open manualDataInit.sql below in MySQL workbench and execute the script

    ```
        KakiGoWhere
        ├── KakiGoWhere-Backend
        │   ├── data_init
        │   │   ├── manualDataInit.sql
    ```

6. Go back to **STEP 9** of Getting started
