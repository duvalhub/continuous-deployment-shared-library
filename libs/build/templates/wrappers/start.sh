#!/bin/sh

if [ -d /run/secrets/ ]; then
  tmp_file=$(mktemp)
  for file in /run/secrets/ENVIRONMENT_*; do
    echo "Sourcing $file"
    sed 's/^/export /g' "$file" >"$tmp_file"
    . "$tmp_file"
  done
  rm -f "$tmp_file"
fi

exec "$@"
