#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh
source 0authlogin.sh

##--------------------------------------------------------------------------------##
#Usage: find & replace "appxxx" with the actual application name.
#Replace APP_FILE_NAME name "sample1-1.0.0" to the actual jar file name.
#Required ENVs: $ZONE $LOGINUSER $VM_NAME
##--------------------------------------------------------------------------------##
cat > 5app-redeploy-restart-appxxx-sub.sh <<- 'EOF'
#! /bin/bash
set -o nounset
set -o errexit
export JAVA_HOME="/opt/jdk-xxx"
export PATH="$JAVA_HOME"/bin:$PATH
APP_FILE_NAME=sample1-1.0.0
if [ ! -d "/usr/local/appxxx" ]; then
    echo '/usr/local/appxxx Not exists, so mkdir'
    mkdir -p /usr/local/appxxx
else
    echo '/usr/local/appxxx exists'
fi
if [ ! -d "/usr/local/appxxx/conf" ]; then
    echo '/usr/local/appxxx/conf Not exists, so mkdir'
    mkdir -p /usr/local/appxxx/conf
else
    echo '/usr/local/appxxx/conf exists'
fi
if [[ "$PWD" == "/usr/local/appxxx" ]]; then
  echo ""
else
  echo "copy."
  cp -f $APP_FILE_NAME.jar 5app-redeploy-restart-appxxx-sub.sh /usr/local/appxxx/
  cd /usr/local/appxxx
fi
#ps -ef | grep APP_FILE_NAME | grep -v grep | awk '{print $2}' | xargs kill -9
#ps -aux | grep APP_FILE_NAME | grep -v grep | awk '{print $2}' | xargs kill -9
#kill -s 9 `pgrep APP_FILE_NAME`
PID=`ps -eaf | grep $APP_FILE_NAME | grep -v grep | awk '{print $2}'`
if [[ "" != "$PID" ]]; then
  echo "killing $PID"
  kill -9 $PID
fi
nohup java -server -Dspring.profiles.active=dev -jar $APP_FILE_NAME.jar > /dev/null 2>&1 &
sleep 10
PID=`ps -eaf | grep $APP_FILE_NAME | grep -v grep | awk '{print $2}'`
echo "Started PID=$PID"
EOF

gcloud compute scp --zone $ZONE --internal-ip "5app-redeploy-restart-appxxx-sub.sh" $LOGINUSER@$VM_NAME:$REMOTE_FOLDER/
gcloud compute ssh $VM_NAME --zone $ZONE --internal-ip --command "cd $REMOTE_FOLDER && chmod 777 5app-redeploy-restart-appxxx-sub.sh && sudo ./5app-redeploy-restart-appxxx-sub.sh"
rm 5app-redeploy-restart-appxxx-sub.sh
echo "Remote appxxx redeployed"
