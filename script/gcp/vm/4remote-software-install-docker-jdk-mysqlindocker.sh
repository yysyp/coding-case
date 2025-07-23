#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh
source 0authlogin.sh

cat > 4remote-software-install-docker-jdk-mysqlindocker-sub.sh <<- 'EOF'
#! /bin/bash
set -o nounset
set -o errexit
#sed -i 's/\r$//' *.sh

tar -xzvf dockerxxx.tgz
cp -f docker/* /usr/bin/
nohup dockerd > /dev/null 2>&1 &
echo 'Docker installed & started'

tar -xzvf OpenJDKxxx.tar.gz -C /opt
echo 'export JAVA_HOME="/opt/jdk-xxx"' >> ~/.bash_profile
echo 'PATH="$JAVA_HOME"/bin:$PATH' >> ~/.bash_profile
source ~/.bash_profile
java -version
echo 'Jdk installed'

APP_NAME=mysql57-with-init-sql
docker login -u $1 -p $2 nexusxxx:${YOUR_PASS}
CONTAINERID=$(docker ps -a| grep $APP_NAME | awk '{print $1}')
if [ -n "$CONTAINERID" ]; then
  echo "Stopping & remove mysql"
  docker rm -f $CONTAINERID
else
  echo "Mysql is not running"
fi

IMAGEID=$(docker images| grep $APP_NAME | awk '{print $1}')
if [ -n "$IMAGEID" ]; then
  docker rmi -f $APP_NAME
fi

docker build -t $APP_NAME -f docker-mysql/Dockerfile docker-mysql
docker run --name $APP_NAME -e MYSQL_ROOT_PASSWORD=${PASS} -d --add-host=host.docker.internal:host-gateway -p 3306:3306 $APP_NAME
#docker run --name $APP_NAME -e MYSQL_ROOT_PASSWORD=${PASS} -d --add-host=host.docker.internal:host-gateway -p 3306:3306 nexusxxx:${YOUR_PASS}/com/xx/mysql:5.7
sleep 5
CONTAINERID=$(docker ps -a| grep $APP_NAME | awk '{print $1}')
echo "docker mysql installed CONTAINERID=$CONTAINERID"


if [ ! -d "/usr/local/app1" ]; then
    echo 'Not exists, so mkdir'
    mkdir -p /usr/local/app1
else
    echo '/usr/local/app1 exists'
fi

EOF

gcloud compute scp --zone $ZONE --internal-ip "4remote-software-install-docker-jdk-mysqlindocker-sub.sh" $LOGINUSER@$VM_NAME:$REMOTE_FOLDER/
gcloud compute ssh $VM_NAME --zone $ZONE --internal-ip --command "cd $REMOTE_FOLDER && chmod 777 4remote-software-install-docker-jdk-mysqlindocker-sub.sh && sudo ./4remote-software-install-docker-jdk-mysqlindocker-sub.sh $USERNAME $PASSWD"
rm 4remote-software-install-docker-jdk-mysqlindocker-sub.sh
echo "Remote software installed"
