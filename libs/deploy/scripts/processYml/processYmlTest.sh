#!/bin/bash

export STACK_NAME="caramba"
export APP_NAME="app_name"
export IMAGE="africa"
export NETWORKS="totoafrica banana isExternal;external"
export VOLUMES="isExternal:/src/etc;external avol2:/src/oekp"
export HOSTS="totoafrica.com"
export ENV_VARIABLES="TOTO=pitou"
export PORT="8080"

echo "####################"
echo "############ Processing"
echo "####################"

temp_file=$(mktemp)
./processYml.sh "$temp_file"

echo "####################"
echo "############ Result"
echo "####################"
cat "$temp_file"
rm -rf "$temp_file"


