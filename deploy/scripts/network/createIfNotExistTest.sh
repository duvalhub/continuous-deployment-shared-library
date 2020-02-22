#!/bin/bash


script_under_test="./createIfNotExist.sh"

uuid=$(uuidgen)
echo "$uuid"

docker network inspect "$uuid" > /dev/null

"$script_under_test" --network "$uuid"
docker network inspect "$uuid" > /dev/null

"$script_under_test" --network "$uuid"


echo "Cleaning..."
docker network rm "$uuid"

