#!/bin/sh

if [ -d /run/secrets/ ]; then
  tmp_file=$(mktemp)
  for file in /run/secrets/*; do
    sed 's/^/source /g' "$file" >"$tmp_file"
    . "$tmp_file"
  dones
  rm -f "$tmp_file"
fi

####################
#sleep infinity
# Why do we not have /entrypoint.sh in our image ????
# We are based on nginx:alpine therefore we should have it.
####################

exec "$@"
