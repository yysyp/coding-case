#! /bin/bash
set -eu

. ./0setenv.sh

cd ../../

#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t doc-zglj-todo20pt:${appversion} -f Dockerfile .
#echo '${PASS}' | docker login --username user123 --password-stdin https://xxx.com:${YOUR_PASS}
#docker tag doc-zglj-todo20pt:${appversion} xxx.com/path/repo/doc-zglj-todo20pt:${appversion}
#docker push xxx.com/path/repo/doc-zglj-todo20pt:${appversion}

docker run --name doc-zglj-todo20pt --rm -itd --add-host=host.docker.internal:host-gateway -p 18961:18961 doc-zglj-todo20pt:${appversion}
