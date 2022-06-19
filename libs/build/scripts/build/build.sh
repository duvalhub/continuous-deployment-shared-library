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
  --build-command) build_command="$2"; shift;;
  --container-version) container_version="$2"; shift;;
  --remove-application-yml) remove_application_yml="$2"; shift;;
  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

test_param "DOCKER_CREDENTIALS_USR"
test_param "DOCKER_CREDENTIALS_PSW"
test_param "IMAGE"
test_param "templates"
test_param "builder"
#test_param "build_destination"
test_param "container"
# Config Server
test_param "APPLICATION_NAME"
test_param "APPLICATION_PROFILES"
test_param "CONFIG_LABEL"
test_param "CONFIG_USERNAME"
test_param "CONFIG_PASSWORD"
assert_param_valid
default_param builder_version latest
default_param container_version alpine
default_param remove_application_yml false

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
  mv "$templates"/containers/"$container"/extras/* ./
  if [ "$remove_application_yml" = "true" ]; then
    rm -f application.yml
  fi
fi

mv "$templates/wrappers/start.sh" ./

echo "### Dockerfile :"
sed -e 's/^/   /' < "$DOCKERFILE"
echo ""

echo "### Version"
docker version
echo "### Building"
export build_destination
export build_command
export APPLICATION_NAME
export APPLICATION_PROFILES
export CONFIG_LABEL
export CONFIG_USERNAME
export CONFIG_PASSWORD
docker build --pull \
--build-arg build_directory=$(mktemp) \
--build-arg build_destination \
--build-arg build_command \
--build-arg APPLICATION_NAME \
--build-arg APPLICATION_PROFILES \
--build-arg CONFIG_LABEL \
--build-arg CONFIG_USERNAME \
--build-arg CONFIG_PASSWORD \
-t "$IMAGE" -f "$DOCKERFILE" .
echo "### Pushing"
docker push "$IMAGE"
