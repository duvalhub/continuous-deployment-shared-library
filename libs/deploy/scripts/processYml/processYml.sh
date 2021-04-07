#!/bin/bash
set -e

###################
# Logs
###################
log() {
    echo "$(date '+%Y-%m-%dT%H:%M:%S') $@"
}
info() {
    log "[INFO] $@"
}
warn() {
    log "[WARNING] $@"
}
error() {
    log "[ERROR] $@"
}

###################
# Params Validation
###################
missing_params=false
test_param() {
    if [ -z "${!1}" ]; then
        warn "Missing '$1' environment variable. Fatal error"
        missing_params=true
    else
        info "Param '$1' value '${!1}"
    fi
}
validate_param(){
    if [ "$missing_params" = true ]; then
        error "Missing parameters detected. Aborting..."
        exit 1
    fi
}
test_param "STACK_NAME"
test_param "APP_NAME"
test_param "IMAGE"
validate_param

###################
# Utils
###################
add_external_thing() {
    local thing="$1"
    local thing_ref="${2:-internal}"
    local external="$3"
    if [ -n "$external" ]; then
      yq w -i "$TMP_YML" "$thing.$thing_ref.name" "$thing_ref"
      yq w -i "$TMP_YML" "$thing.$thing_ref.external" "true"
    else
      yq w -i "$TMP_YML" "$thing.$thing_ref.name" "$STACK_NAME"_"$thing_ref"
    fi
}
add_thing_to_service() {
  local thing="$1"
  local IFS=';'
  read -ra PARAMS <<< "$2"
  local value="${PARAMS[0]}"
  local external="${PARAMS[1]}"
  IFS=':'
  read -ra PARAMS <<< "$value"
  local key="${PARAMS[0]}"
  local base_path="$3"
  yq w -i "$TMP_YML" "$base_path"."$thing""[+]" "$value"
  add_external_thing "$thing" "$key" "$external"
}
add_env_vars(){
  local IFS=$'\n'
  for env in $ENV_VARIABLES; do
    yq w -i "$TMP_YML" "$BASE_PATH.environment[+]" "$env"
  done
}
###################
# Prepare
###################
log "Creating docker-compose.yml file named '$1'"
TMP_YML=$(mktemp)
BASE_PATH="services.$APP_NAME"

#####################
# Begin writing files
#####################
yq n version \"3.8\" > "$TMP_YML"

#####################
# Image
yq w -i "$TMP_YML" "$BASE_PATH.image" "$IMAGE"

#####################
# Environments
if [ -n "$PORT" ]; then
    yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.port\"" "$PORT"
fi


if [ -n "$HOSTS" ]; then
    yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.host\"" "\"$HOSTS\""
    yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.ssl\"" "\"true\""
fi

if [ -n "$ENV_VARIABLES" ]; then
  add_env_vars
fi

#####################
# Networks
if [ -n "$NETWORKS" ]; then
    for network in $NETWORKS; do
        add_thing_to_service "networks" "$network" "$BASE_PATH"
    done
fi

if [ -n "$HOSTS" ]; then
    add_thing_to_service "networks" "reverseproxy;external" "$BASE_PATH"
fi

#####################
# Volumes
if [ -n "$VOLUMES" ]; then
    for volume in $VOLUMES; do
        add_thing_to_service "volumes" "$volume" "$BASE_PATH"
    done
fi

#####################
sed -i -e "s/'\"/\"/g" -e "s/\"'/\"/g" "$TMP_YML"

#####################
echo "### Result : "
cat "$TMP_YML"

if [ -n "$1" ]; then
    cat "$TMP_YML" > "$1"
    echo "### Wrote yml file succesfully."
fi

rm -f "$TMP_YML"