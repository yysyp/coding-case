#! /bin/bash
set -eu

. ./0setenv.sh

cd ../../

#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t my-test-pg:${appversion} -f Dockerfile .
#echo '${PASS}' | docker login --username user123 --password-stdin https://xxx.com:${YOUR_PASS}
#docker tag my-test-pg:${appversion} xxx.com/path/repo/my-test-pg:${appversion}
#docker push xxx.com/path/repo/my-test-pg:${appversion}

docker run --name my-test-pg --rm -itd --add-host=host.docker.internal:host-gateway -p 16622:16622 my-test-pg:${appversion}
