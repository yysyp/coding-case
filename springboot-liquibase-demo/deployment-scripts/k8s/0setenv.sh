#! /bin/bash
set -eu

echo "Please set NEXUS_USER & NEXUS_PASS if you'd like to connect to NEXUS."
export NEXUS_USER=user1
export NEXUS_PASS=`cat /d/patrick/NEXUS_PASS.txt`

if [ -z "$NEXUS_PASS" ]; then
  read -s -p "Please input NEXUS_PASS: " NEXUS_PASS
fi

