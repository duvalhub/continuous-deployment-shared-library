#!/bin/bash

under_test="$(pwd)/start.sh"

export ENVIRONMENT_FILES="env-test.properties"

"$under_test"

echo "$TOTO"