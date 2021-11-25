#!/usr/bin/env sh
NGINX_GEN_NAME="nginx-file-replace"
NGINX_GEN_VERSION=1.0.0
wget "https://github.com/duvalhub/nginx-file-replace/archive/refs/tags/$NGINX_GEN_VERSION.tar.gz"
tar -xzf "$NGINX_GEN_VERSION.tar.gz"
mv "$NGINX_GEN_NAME-$NGINX_GEN_VERSION/nginx.tmpl" ./
mv "$NGINX_GEN_NAME-$NGINX_GEN_VERSION/bin/nginx-gen" ./
chmod +x nginx-gen