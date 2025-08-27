#! /bin/bash
set -eu

. ./0setenv.sh

cd ../../

#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t springboot-liquibase-demo:${appversion} -f Dockerfile .
#echo '${PASS}' | docker login --username user123 --password-stdin https://xxx.com:${YOUR_PASS}
#docker tag springboot-liquibase-demo:${appversion} xxx.com/path/repo/springboot-liquibase-demo:${appversion}
#docker push xxx.com/path/repo/springboot-liquibase-demo:${appversion}

docker run --name springboot-liquibase-demo --rm -itd --add-host=host.docker.internal:host-gateway -p 19097:19097 springboot-liquibase-demo:${appversion}
