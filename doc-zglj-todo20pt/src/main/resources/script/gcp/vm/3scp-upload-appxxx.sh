#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh
source 0authlogin.sh

#gcloud compute scp --zone $ZONE --internal-ip --recurse "$LOCAL_FOLDER" $LOGINUSER@$VM_NAME:
#gcloud compute scp --zone $ZONE --internal-ip "$GCP_CREDENTIAL" $LOGINUSER@$VM_NAME:$REMOTE_FOLDER/
#echo "Folder $LOCAL_FOLDER uploaded to remote VM"

gcloud compute scp --zone $ZONE --internal-ip --recurse "source-folder" $LOGINUSER@$VM_NAME:$REMOTE_FOLDER/
gcloud compute scp --zone $ZONE --internal-ip "$GCP_CREDENTIAL" $LOGINUSER@$VM_NAME:$REMOTE_FOLDER/
echo "Folder source-folder uploaded to remote VM"
