#!/bin/sh

for file in /run/secrets/*; do
  . "$file"
done

"$@"
