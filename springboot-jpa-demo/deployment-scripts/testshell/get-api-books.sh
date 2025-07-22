#! /bin/bash
set -eu

curl -ikLv --request GET \
  --url http://localhost:10001/api/books/ \
  --header 'User-Agent: insomnia/10.1.0'

echo ' '
read -p "Press any key..."

