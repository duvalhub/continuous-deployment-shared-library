#!/bin/bash

while [[ "$#" -gt 0 ]]; do case $1 in
  -n|--network) network="$2"; shift;;
  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

echo "### Create, if not exists, network '$network'"

docker network inspect "$network" > /dev/null

result="$?"
if [ "$result" = 0 ]; then
    echo "Network '$network' already exists"
    exit 0
fi

echo "Network does not exist. Creating network '$network'"
docker network create -d overlay "$network"
