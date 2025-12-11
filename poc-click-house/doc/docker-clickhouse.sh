#! /bin/bash

docker pull yandex/clickhouse-server
#docker run -d --name clickhouse-server -p 8123:8123 -p 9000:9000 --ulimit nofile=262144:262144 yandex/clickhouse-server

docker run --name clickhouse-server -p 8123:8123 -p 9000:9000 --ulimit nofile=262144:262144 yandex/clickhouse-server


