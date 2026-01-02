#!/bin/bash

branchName="feature/Abc-123456_addLogin"

if [[ $(echo "$branchName" | grep -oP "(?<=Abc-)\d+") == "123456" ]]; then
  echo "it's 123456";
else
  echo "it's not 123456";
fi

echo "done."
