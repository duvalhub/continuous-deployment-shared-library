#!/bin/bash
set -e

missing_params=false
test_param() {
    if [ -z "$1" ]; then
        echo "Missing '$2' environment variable. Fatal error"
        missing_params=true
    fi
}
test_param "$DOCKER_CREDENTIALS_USR" "DOCKER_CREDENTIALS_USR"
test_param "$DOCKER_CREDENTIALS_PSW" "DOCKER_CREDENTIALS_PSW"
test_param "$IMAGE" "IMAGE"
if [ "$missing_params" = true ]; then
    echo "Missing parameters detected. Aborting..."
    exit 1
fi

while [[ "$#" -gt 0 ]]; do case $1 in
  -t|--templates) templates="$2"; shift;;
  -b|--builder) builder="$2"; shift;;
  -d|--build-destination) build_destination="$2"; shift;;
  -c|--container) container="$2"; shift;;
  -v|--builder-version) builder_version="$2"; shift;;
  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

echo "### Builder: '$builder', Container: '$container', BuildDestination: '$build_destination'"
DOCKERFILE=$(mktemp)

if [ -z "$build_destination" ]; then
  export build_destination="build"
else
  export build_destination
fi

cat "$templates/builders/$builder/Dockerfile" > $DOCKERFILE
echo "" >> $DOCKERFILE
cat "$templates/containers/$container/Dockerfile" >> $DOCKERFILE

if [ -d "$templates/containers/$container/extras" ]; then
  mv $templates/containers/$container/extras/* ./
fi

echo "### Dockerfile :"
cat $DOCKERFILE | sed -e 's/^/   /'
echo ""
echo "### Version"
docker version
echo "### Building"
docker build --build-arg build_directory=$(mktemp) --build-arg build_destination --build-arg builder_version -t "$IMAGE" -f $DOCKERFILE .
echo "### Login in"
echo "$DOCKER_CREDENTIALS_PSW" | docker login --username "$DOCKER_CREDENTIALS_USR" --password-stdin
echo "### Pushing"
docker push "$IMAGE"
