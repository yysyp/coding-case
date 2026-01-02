#! /bin/bash
set -eu

kubectl get secret $1 -n nsxxx -o yaml > ./ignore/xxx_$(date +"%Y-%m-%d_%H%M%S").txt

#./kubeget-secret.sh secretname1
