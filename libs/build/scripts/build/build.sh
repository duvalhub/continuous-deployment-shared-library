#!/bin/bash
set -e

#################
# Utils
################
missing_params=false
test_param() {
    if [ -z "${!1}" ]; then
        echo "Missing '$1' environment variable. Fatal error"
        missing_params=true
    fi
}
assert_param_valid() {
  if [ "$missing_params" = true ]; then
      echo "Missing parameters detected. Aborting..."
      exit 1
  fi
}
default_param() {
    if [ -z "${!1}" ]; then
        echo "Optional '$1' param missing. Using default '$2'."
        eval "$1=$2"
    fi
}

#################
# Script Params
################
while [[ "$#" -gt 0 ]]; do case $1 in
  -t|--templates) templates="$2"; shift;;
  -b|--builder) builder="$2"; shift;;
  -d|--build-destination) build_destination="$2"; shift;;
  -c|--container) container="$2"; shift;;
  --builder-version) builder_version="$2"; shift;;
  --container-version) container_version="$2"; shift;;
  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

test_param "DOCKER_CREDENTIALS_USR"
test_param "DOCKER_CREDENTIALS_PSW"
test_param "IMAGE"
test_param "templates"
test_param "builder"
test_param "container"
assert_param_valid
default_param build_destination build
default_param builder_version latest
default_param container_version alpine

#################
# Begin Script
################
echo "### Builder: '$builder:$builder_version', Container: '$container:$container_version', BuildDestination: '$build_destination'"
DOCKERFILE=$(mktemp)

{
  echo "ARG builder_version=$builder_version"
  echo "ARG container_version=$container_version"
  cat "$templates/builders/$builder/Dockerfile"
  echo ""
  cat "$templates/containers/$container/Dockerfile"
}  > "$DOCKERFILE"

if [ -d "$templates/containers/$container/extras" ]; then
  mv "$templates/containers/$container/extras/*" ./
fi

echo "### Dockerfile :"
sed -e 's/^/   /' < "$DOCKERFILE"
echo ""

echo "### Version"
docker version
echo "### Building"
docker build --build-arg build_directory=$(mktemp) --build-arg build_destination --build-arg builder_version -t "$IMAGE" -f $DOCKERFILE .
echo "### Pushing"
docker push "$IMAGE"
