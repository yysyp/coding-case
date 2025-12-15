#! /bin/bash
set -o nounset
set -o errexit
source 0env-set.sh

#rm -rf $LOCAL_FOLDER/aa/
#git clone --branch main --single-branch https://xxx/aa.git $LOCAL_FOLDER/aa
#echo "Git cloned to $LOCAL_FOLDER for uploading"

rm -rf $LOCAL_FOLDER/appxxx
git clone --branch main --single-branch https://xxx/aa.git $LOCAL_FOLDER/appxxx
echo "Git cloned to $LOCAL_FOLDER/appxxx for uploading"
