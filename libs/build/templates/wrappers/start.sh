#!/bin/sh

if [ -d /run/secrets/ ]; then
  for file in /run/secrets/*; do
    . <(sed 's/^/source /g' "$file")
  done
fi

####################
#sleep infinity
# Why do we not have /entrypoint.sh in our image ????
# We are based on nginx:alpine therefore we should have it.
####################

exec "$@"
