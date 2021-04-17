#!/bin/bash

export STACK_NAME="caramba"
export APP_NAME="app_name"
export IMAGE="africa"
export NETWORKS="totoafrica banana isExternal;external"
export VOLUMES="isExternal:/src/etc;external avol2:/src/oekp"
export HOSTS="totoafrica.com"
export ENV_VARIABLES="TOTO=pitou"
export SECRETS="MY_SECRET"
export PORT="8080"
export REPLICAS="1"
export DATABASE_IMAGE="mysql"
export DATABASE_VERSION="10.5"
export DATABASE_SECRET="MYSQL_SECRET"

echo "####################"
echo "############ Processing"
echo "####################"

temp_file=$(mktemp)
./writeCompose.sh "$temp_file"

echo "####################"
echo "############ Result"
echo "####################"
cat "$temp_file"
rm -rf "$temp_file"


