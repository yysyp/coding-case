#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh
source 0authlogin.sh

gcloud beta compute instances create $VM_NAME --project=$PROJECT_ID --zone=$ZONE --machine-type=n1-standard-2 \
--network-interface=xxx \
--service-account=$SA_ACCOUNT
echo "VM created. vm=$VM_NAME"
echo "gcloud compute ssh $VM_NAME --zone $ZONE --internal-ip"
