#!/bin/bash
set -e

echo "Login into registry"
echo "$DOCKER_CREDENTIALS_PSW" | docker login --username "$DOCKER_CREDENTIALS_USR" --password-stdin
cat $HOME/.docker/config.json
echo "Deploying app"
docker stack deploy -c "$COMPOSE_FILE_PATH" "$STACK_NAME"
