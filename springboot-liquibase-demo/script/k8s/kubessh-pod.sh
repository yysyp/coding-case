#! /bin/bash
set -eu

POD=$(kubectl -n nsxx get pods --no-headers -o custom-columns=":metadata.name" | grep xxx)
RESULT=$(kubectl -n nsxx exec -it $POD -- bash -c "cat /config/xxx.pk8 | base64 -w0")
echo $RESULT > ./ignore/xxx.pk8_$(date +"%Y-%m-%d_%H%M%S").txt
winpty kubectl -n nsxx exec -it $POD -- bash
