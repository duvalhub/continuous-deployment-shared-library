#!/bin/bash
set -e

echo "Login into registry"
echo "$DOCKER_CREDENTIALS_PSW" | docker login --username "$DOCKER_CREDENTIALS_USR" --password-stdin

echo "Deploying app"
docker stack deploy -c ${composeFilePath} ${request.getStackName()}"
