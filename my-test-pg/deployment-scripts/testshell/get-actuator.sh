#! /bin/bash
set -eu

curl -ikLv --request GET \
  --url http://localhost:16622/actuator \
  --header 'User-Agent: insomnia/10.1.0'

echo ' '
read -p "Press any key..."

