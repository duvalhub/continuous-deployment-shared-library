#!/bin/bash
echo "### Running in gitClone.sh"
echo "### GIT_URL: '$GIT_URL', GIT_DIRECTORY: '$GIT_DIRECTORY', GIT_SSH_COMMAND: '$GIT_SSH_COMMAND'"
rm -rf "$GIT_DIRECTORY"
git --version
git clone "$GIT_URL" "$GIT_DIRECTORY"
if [ "$?" -gt 0 ]; then
  echo "Failed to clone $GIT_URL"
  exit 1
fi
pushd "$GIT_DIRECTORY"
git config --local user.email "${GIT_EMAIL:-"robot@email.com"}"
git config --local user.name "${GIT_NAME:-"Robot"}"
popd