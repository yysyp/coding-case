#! /bin/bash

echo "Loop monitor $1"
current_datatime_str=$(date +"%Y-%m-%d_%H%M%S")
output_file="./ignore/output_$current_datatime_str.log"
start_time=$(date +%s)

while [ $(($(date +%s) - start_time)) -lt 30 ]; do
  echo "." >> "$output_file"
  date >> "$output_file"
  #kubectl -n nsxx describe deployment $1 >> "$output_file"
  sleep 5
done
