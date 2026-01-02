#! /bin/bash
set -eu

. ./0setenv.sh

cd ../../

#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t aspringboot-jpa-demo:${appversion} -f Dockerfile .
#echo '${PASS}' | docker login --username user123 --password-stdin https://xxx.com:${YOUR_PASS}
#docker tag aspringboot-jpa-demo:${appversion} xxx.com/path/repo/aspringboot-jpa-demo:${appversion}
#docker push xxx.com/path/repo/aspringboot-jpa-demo:${appversion}

docker run --name aspringboot-jpa-demo --rm -itd --add-host=host.docker.internal:host-gateway -p 10001:10001 aspringboot-jpa-demo:${appversion}
