#!/bin/bash

declare env_files="${ENVIRONMENT_FILES:-""}"

for file in $env_files; do
  mkfifo /tmp/source_fifo;
  sed 's/^/export /g' "$file" > /tmp/source_fifo & source /tmp/source_fifo
  rm -f /tmp/source_fifo
done

"$@"