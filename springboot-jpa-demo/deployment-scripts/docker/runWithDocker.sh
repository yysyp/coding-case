#! /bin/bash
set -eu

. ./0setenv.sh

cd ../../

#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t springboot-jpa-demo:${appversion} -f Dockerfile .
#echo 'password123' | docker login --username user123 --password-stdin https://xxx.com:12345
#docker tag springboot-jpa-demo:${appversion} xxx.com/path/repo/springboot-jpa-demo:${appversion}
#docker push xxx.com/path/repo/springboot-jpa-demo:${appversion}

docker run --name springboot-jpa-demo --rm -itd --add-host=host.docker.internal:host-gateway -p 10001:10001 springboot-jpa-demo:${appversion}
