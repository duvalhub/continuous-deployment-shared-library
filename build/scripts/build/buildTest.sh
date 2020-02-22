#!/bin/sh
set -e

export PIPELINE_WORKDIR="/Users/huguesmcd/workspace/duvalhub/continous-deployment"
export TEMPLATES_PATH="$PIPELINE_WORKDIR/build/templates"

export BUILDER="react"
export CONTAINER="nginx"


./build.sh --templates "$TEMPLATES_PATH" --builder "$BUILDER" --container "$CONTAINER"