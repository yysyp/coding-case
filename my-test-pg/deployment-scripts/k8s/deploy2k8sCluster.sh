#! /bin/bash
set -eu

. ./0setenv.sh

kubectl apply -f deploy.yaml
