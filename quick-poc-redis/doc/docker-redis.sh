#! /bin/bash

#export USER_NAME=`cat ./ignore/user.txt`
#export PASS_WORD=`cat ./ignore/pass.txt`
#echo "$PASS_WORD" | docker login nexus.xxx:0000 --username "$USER_NAME" --password-stdin
#docker pull nexus.xxx:0000/redis:6.2.6
#docker tag nexus.xxx:0000/redis:6.2.6 redis:6.2.6
#docker run --restart=always -p 6379:6379 redis:6.2.6 redis-server

docker pull redis
docker run --restart=always -p 6379:6379 --name my-redis -d redis redis-server

