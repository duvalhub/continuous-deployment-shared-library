#!/usr/bin/env sh
# Sourcing secrets prefixed by 'ENVIRONMENT_'
if [ -d /run/secrets/ ]; then
  tmp_file=$(mktemp)
  for file in /run/secrets/ENVIRONMENT_*; do
    echo "Sourcing $file"
    sed 's/^/export /g' "$file" >"$tmp_file"
    . "$tmp_file"
  done
  rm -f "$tmp_file"
fi

# Equivalent to 'exec $@' but sometimes processes don't handle HANGUP signal well, like mariadb.
exec $@
#$@ &
#PID="$!"
#trap "kill -SIGTERM $PID" SIGINT SIGTERM
#wait