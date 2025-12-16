#! /bin/bash
set -eu

./dockerBuildPushAndGenDeployYaml.sh
./deploy2k8sCluster.sh

