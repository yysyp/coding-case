#! /bin/bash

#docker pull redis:6.2.6
docker pull redis
docker run --restart=always -p 6379:6379 --name my-redis -d redis redis-server


