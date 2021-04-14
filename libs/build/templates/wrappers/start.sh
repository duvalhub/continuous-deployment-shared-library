#!/bin/sh

for file in /run/secrets/*; do
  . "$file"
done

####################
sleep 100000000000
# Why do we not have /entrypoint.sh in our image ????
# We are based on nginx:alpine therefore we should have it.
####################

exec "$@"
