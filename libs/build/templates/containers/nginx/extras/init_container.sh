#!/usr/bin/env sh

process_files() (
  cd /usr/share/nginx/html || exit 1
  nginx-gen
)
process_files
/docker-entrypoint.sh nginx -g "daemon off;"
