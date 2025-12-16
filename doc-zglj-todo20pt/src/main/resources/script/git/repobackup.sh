#!/bin/bash

echo "Begin to backup repo $1"
ts=$(date +"%Y-%m-%d_%H%M%S")

cd $1

for branch in $(git branch -r | grep -v '\->'); do
  git branch --track "${branch#origin/}" "$branch" || true
done

git fetch --all
git pull --all

cd ..
tar -czvf "$1"_"$ts"_bak.tar.gz $1

