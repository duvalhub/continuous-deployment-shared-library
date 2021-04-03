#!/bin/bash
set -e
git fetch
release_branch_count=$(git branch -a | grep -c '^  remotes/origin/release');

if (( release_branch_count > 0 )); then
    version=$(git branch -a | grep '^  remotes/origin/release' | awk -F"release/" '{print $2}')
    echo -n "release/$version"
else 
    >&2 echo "There is no release branch. Failing" 
    exit 1
fi
