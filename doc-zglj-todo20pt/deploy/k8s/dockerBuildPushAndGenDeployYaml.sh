#! /bin/bash
set -eu

. ./0setenv.sh
appversion=$(date +"%Y%m%d-%H%M%S")
CURDIR=$(pwd)

cd ../../
#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t doc-zglj-todo20pt:${appversion} -f Dockerfile .
#gcloud auth configure-docker xxx.com
docker tag doc-zglj-todo20pt:${appversion} xxx.com/path/repo/doc-zglj-todo20pt:${appversion}

#docker push xxx.com/path/repo/doc-zglj-todo20pt:${appversion}

cd "${CURDIR}"
cp -f deploy-template.yaml deploy.yaml
sed -i "s/THE_APP_VERSION/${appversion}/g" deploy.yaml

