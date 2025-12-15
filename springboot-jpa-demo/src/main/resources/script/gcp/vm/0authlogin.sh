#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh

current_datetime_str=$(date +"%Y-%m-%d %H:%M:%S")

if [ ! -e "./authLogin.output" ]; then
    touch ./authLogin.output
    echo "1000-01-01 00:00:00" > ./authLogin.output
fi

last_datetime_str=`cat ./authLogin.output`
last_datetime=$(date -d "$last_datetime_str" +%s)
current_datetime=$(date -d "$current_datetime_str" +%s)
if (( $current_datetime - $last_datetime < 86400 )); then
  echo "Auth within 1 day."
  gcloud config set account $ACCOUNT_ID
  gcloud config set project $PROJECT_ID
else
  echo "To login"
  gcloud config set account $ACCOUNT_ID
  gcloud auth login
  gcloud config set project $PROJECT_ID
  echo "$current_datetime_str" > ./authLogin.output
fi
gcloud config set auth/impersonate_service_account xxxx