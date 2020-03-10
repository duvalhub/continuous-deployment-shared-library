#!/bin/bash

origin_position=$(pwd)
script_under_test=$(realpath ./getReleaseBranch.sh)

release_branch="$1"

if [ -z "$release_branch" ]; then
    release_branch="release/1.2.3"
fi

# Arrange
temp_git_dir1=$(mktemp -d)
temp_git_dir2=$(mktemp -d)
temp_repo_dir=$(mktemp -d)

cd $temp_repo_dir
git init --bare

cd $temp_git_dir1
git init
git remote add origin $temp_repo_dir
git checkout -b develop
touch allo
git add allo
git commit -am "Not important"
git push origin develop

cd $temp_git_dir2
git init
git remote add origin $temp_repo_dir
git pull
git checkout develop
git checkout -b $release_branch
git push origin $release_branch

# Act
cd $temp_git_dir1
chmod +x $script_under_test
found_release_branch=$($script_under_test)

# Assert
test_success=false

if [ "$found_release_branch" = "$release_branch" ]; then
    echo "Success!"
    test_success=true
else
    echo "Test failed!"
    test_success=false
fi

echo "Cleaning..."
cd $origin_position
rm -rf $temp_repo_dir
rm -rf $temp_git_dir2
rm -rf $temp_git_dir2
echo "Clean done."

if [ "$test_success" != true ]; then
    exit 1
fi