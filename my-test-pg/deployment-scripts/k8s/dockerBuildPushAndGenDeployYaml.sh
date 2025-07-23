#! /bin/bash
set -eu

. ./0setenv.sh
appversion=$(date +"%Y%m%d-%H%M%S")
CURDIR=$(pwd)

cd ../../
#echo "Make sure you mvn rebuild the latest jar!!!"
mvn clean package

docker build -t my-test-pg:${appversion} -f Dockerfile .
#gcloud auth configure-docker xxx.com
docker tag my-test-pg:${appversion} xxx.com/path/repo/my-test-pg:${appversion}

#docker push xxx.com/path/repo/my-test-pg:${appversion}

cd "${CURDIR}"
cp -f deploy-template.yaml deploy.yaml
sed -i "s/THE_APP_VERSION/${appversion}/g" deploy.yaml

