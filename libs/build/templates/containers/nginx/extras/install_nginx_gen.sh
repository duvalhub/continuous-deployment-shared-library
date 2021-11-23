#!/usr/bin/env sh


NGINX_GEN_VERSION=feature/initial
wget "https://github.com/duvalhub/nginx-file-replace/raw/$NGINX_GEN_VERSION/nginx.tmpl"
wget "https://github.com/duvalhub/nginx-file-replace/raw/$NGINX_GEN_VERSION/bin/nginx-gen"
chmod +x nginx-gen