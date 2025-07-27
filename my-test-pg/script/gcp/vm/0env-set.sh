#! /bin/bash
set -o nounset
set -o errexit
#sed -i 's/\r$//' *.sh

PROJECT_ID=xxx
ZONE=xxx
VM_NAME=my-vm-001
SA_ACCOUNT=auto_deploy@xxx.com
LOCAL_FOLDER=/c/Users/yp/apps
REMOTE_FOLDER=.
LOGINUSER=ubuntu
USERNAME=nexus1
PASSWD=`cat /c/Users/yp/keys/nexus1_passwd.txt`
GCP_CREDENTIAL=/c/Users/yp/gcp_credential.json

echo "Env set. VM_NAME=$VM_NAME"