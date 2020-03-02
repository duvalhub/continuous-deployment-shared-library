#!/bin/bash
set -e
echo "Deploying app"
docker stack deploy --with-registry-auth -c "$COMPOSE_FILE_PATH" "$STACK_NAME"
