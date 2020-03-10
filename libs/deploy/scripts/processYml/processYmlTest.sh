#!/bin/bash

export STACK_NAME="caramba"
export APP_NAME="build"
export IMAGE="africa"

export VOLUMES="isExternal:/src/etc:external avol2:/src/oekp"

echo "####################"
echo "############ Without Hosts"
echo "####################"
./processYml.sh

echo "####################"
echo "############ With Hosts"
echo "####################"

export HOSTS="totoafrica.com"
./processYml.sh

echo "####################"
echo "############ With Env Vars"
echo "####################"
export ENV_VARIABLES="TOTO=pitou
DAMN=toto oijo oijoij"
./processYml.sh


echo "####################"
echo "############ Result"
echo "####################"

temp_file=$(mktemp)
./processYml.sh "$temp_file" > /dev/null
cat "$temp_file"
rm -rf "$temp_file"


