#!/bin/bash

declare env_files="${ENVIRONMENT_FILES:-""}"

for file in $env_files; do
  source <(sed 's/^/export /g' "$file")
done

"$@"