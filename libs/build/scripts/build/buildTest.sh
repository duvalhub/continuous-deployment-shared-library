#!/bin/sh
set -e

export DOCKER_CREDENTIALS_USR="toto"
export DOCKER_CREDENTIALS_PSW="toto"
export IMAGE="latest"


export PIPELINE_WORKDIR="/Users/huguesmcd/workspace/duvalhub/continuous-deployment-shared-library"
export TEMPLATES_PATH="$PIPELINE_WORKDIR/libs/build/templates"

export BUILDER="react"
export CONTAINER="nginx"


./build.sh --templates "$TEMPLATES_PATH" --builder "$BUILDER" --container "$CONTAINER"