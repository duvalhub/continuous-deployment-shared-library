#!/bin/bash
echo "### Running in gitClone.sh"
echo "### GIT_URL: '$GIT_URL', GIT_DIRECTORY: '$GIT_DIRECTORY', GIT_SSH_COMMAND: '$GIT_SSH_COMMAND'"
rm -rf "$GIT_DIRECTORY"
git --version
git clone "$GIT_URL" "$GIT_DIRECTORY"
pushd "$GIT_DIRECTORY"
git config --local user.email "${GIT_EMAIL:-"toto-africa@email.com"}"
git config --local user.name "${GIT_NAME:-"Toto Africa"}"
popd