#!/bin/bash

### dos2linux:
  - dos2unix *.sh
  - find . -type f -name "*.sh" -exec sed -i 's/\r$//' {} +

### chmod +x *.sh

### docker issues:
Docker proxy setting could end up with TLS error when run command in docker containers:
curl(35) TLS connect error: error:00000000:lib(0)::reason(0)

Docker shared folder path to docker container could auto add the ";C"
You need to remove the ";C" in the shared folder path via docker UI.


