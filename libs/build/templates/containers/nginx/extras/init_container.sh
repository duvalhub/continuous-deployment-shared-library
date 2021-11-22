#!/usr/bin/env sh

process_files() (
  cd /usr/share/nginx/html
  nginx-gen
)

/docker-entrypoint.sh nginx -g "daemon off;"
