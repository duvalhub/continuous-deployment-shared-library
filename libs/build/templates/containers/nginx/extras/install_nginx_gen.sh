#!/usr/bin/env bash

apt-get update
apt-get install -y wget
wget https://github.com/duvalhub/nginx-file-replace/raw/feature/initial/nginx-gen2
mv nginx-gen2 nginx-gen