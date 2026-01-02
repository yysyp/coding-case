#! /bin/bash
set -eu

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

