#!/bin/bash

url = "http://xxxx"
response=$(curl -s -X POST "$url" \
  -H "Content-Type: application/json" \
  -d '{ "username": "$1", "password": "$2" }' \
)

token = $(echo "$response" | grep -oP '"token"\s*:\s*"\K[^"]+')

if [ -n "$token" ]; then
  echo "token: $token";
else
  echo "token not found";
fi

