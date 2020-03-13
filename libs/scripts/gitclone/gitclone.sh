#!/bin/bash
echo "### Running in gitClone.sh"
echo "### GIT_URL: '$GIT_URL', GIT_DIRECTORY: '$GIT_DIRECTORY', GIT_SSH_COMMAND: '$GIT_SSH_COMMAND'"
rm -rf "$GIT_DIRECTORY"
git --version
git clone "$GIT_URL" "$GIT_DIRECTORY"
