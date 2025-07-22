#! /bin/bash
set -eu

curl -ikLv --request GET \
  --url http://localhost:10001/api-docs \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv --request GET \
  --url http://localhost:10001/api/books/ \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv --request GET \
  --url http://localhost:10001/actuator \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv -X 'POST' \
  'http://localhost:10001/api/books/' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Books20241017",
  "author": "ps",
  "price": 170,
  "publishDate": "2024-10-17"
}'


echo ' '
read -p "Press any key..."