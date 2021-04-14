#!/bin/sh

if [ -d /run/secrets/ ]; then
  tmp_file=$(mktemp)
  for file in /run/secrets/*; do
    echo "Sourcing $file"
    sed 's/^/export /g' "$file" >"$tmp_file"
    . "$tmp_file"
#    . "$file"
  done
  rm -f "$tmp_file"
fi
#env
####################
#sleep infinity
# Why do we not have /entrypoint.sh in our image ????
# We are based on nginx:alpine therefore we should have it.
####################

exec "$@"
