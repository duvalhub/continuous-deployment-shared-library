#!/bin/bash
echo "### Running in gitClone.sh"
echo "### GIT_URL: '$GIT_URL', GIT_DIRECTORY: '$GIT_DIRECTORY', GIT_SSH_COMMAND: '$GIT_SSH_COMMAND'"
rm -rf "$GIT_DIRECTORY"
ls -l "$SSH_KEY_PATH"
cat "$SSH_KEY_PATH"
#export GIT_SSH_COMMAND="ssh -oStrictHostKeyChecking=accept-new -i $SSH_KEY_PATH  -F /dev/null" 
git --version
git clone "$GIT_URL" "$GIT_DIRECTORY"
