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
export DATABASE_ENTRYPOINT="init.sh"
export DATABASE_ENTRYPOINT_VOLUME="database-entrypoint"

export HEALTHCHECK_COMMAND="node healthcheck.js"
export HEALTHCHECK_INTERVAL="5s"
export HEALTHCHECK_TIMEOUT="6s"
export HEALTHCHECK_retries="4"
export HEALTHCHECK_START_PERIOD="10"

echo "####################"
echo "############ Processing"
echo "####################"

temp_file=$(mktemp)
"$(dirname -- "$0")/writeCompose.sh" "$temp_file"

echo "####################"
echo "############ Result"
echo "####################"
cat "$temp_file"
rm -rf "$temp_file"


