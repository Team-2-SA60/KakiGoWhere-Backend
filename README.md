
```
docker volume create docker_app_image
```

```
docker run --rm -v docker_app_image:/data -v $(pwd)/../uploads/images:/src busybox sh -c "cp -r /src/* /data/"
```

```
docker cp ../uploads/images/. docker-app-1:/data/
```