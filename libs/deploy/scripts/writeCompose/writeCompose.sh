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
validate_param() {
  if [ "$missing_params" = true ]; then
    error "Missing parameters detected. Aborting..."
    exit 1
  fi
}
test_param "STACK_NAME"
test_param "APP_NAME"
test_param "IMAGE"
test_param "REPLICAS"
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
    if [[ "$thing" == "networks" ]]; then
      yq w -i "$TMP_YML" "$thing.$thing_ref.driver_opts.encrypted" "\"true\""
    fi
  fi
}
add_thing_to_service() {
  local thing="$1"
  IFS=';' read -ra PARAMS <<<"$2"
  local value="${PARAMS[0]}"
  local external="${PARAMS[1]}"
  IFS=':' read -ra PARAMS <<<"$value"
  local key="${PARAMS[0]}"
  local base_path="$3"
  yq w -i "$TMP_YML" "$base_path"."$thing""[+]" "$value"
  add_external_thing "$thing" "$key" "$external"
}
add_env_vars() {
  local IFS=$'\n'
  for env in $ENV_VARIABLES; do
    yq w -i "$TMP_YML" "$BASE_PATH.environment[+]" "$env"
  done
}
add_secret_vars() {
  local IFS=$'\n'
  for secret in $SECRETS; do
    add_thing_to_service "secrets" "$secret;external" "$BASE_PATH"
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
yq n version \"3.8\" >"$TMP_YML"

#####################
# Image
yq w -i "$TMP_YML" "$BASE_PATH.image" "$IMAGE"

yq w -i "$TMP_YML" "$BASE_PATH.deploy.replicas" "$REPLICAS"

#####################
# DNS
if [ -n "$PORT" ]; then
  yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.port\"" "$PORT"
fi

if [ -n "$HOSTS" ]; then
  yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.host\"" "\"$HOSTS\""
  yq w -i "$TMP_YML" "$BASE_PATH.deploy.labels.\"reverseproxy.ssl\"" "\"true\""
fi

# Environments
if [ -n "$ENV_VARIABLES" ]; then
  add_env_vars
fi

# Secrets
if [ -n "$SECRETS" ]; then
  add_secret_vars
fi

# Database
if [ -n "$DATABASE_IMAGE" ] && [ -n "$DATABASE_VERSION" ]; then
  # Create Database Service
  yq w -i "$TMP_YML" "services.database.image" "${DATABASE_IMAGE}:${DATABASE_VERSION}"
  # Entrypoint script
#  yq w -i "$TMP_YML" "services.database.entrypoint" "/${DATABASE_ENTRYPOINT_VOLUME}/${DATABASE_ENTRYPOINT}"
  #  yq w -i "$TMP_YML" "services.database.cmd" "docker-entrypoint.sh mysqld"

  # Volume for entrypoint script
#  add_thing_to_service "volumes" "$DATABASE_ENTRYPOINT_VOLUME:/${DATABASE_ENTRYPOINT_VOLUME}" "services.database"
#  add_thing_to_service "volumes" "$DATABASE_ENTRYPOINT_VOLUME:/${DATABASE_ENTRYPOINT_VOLUME};external" "services.database"

  # Database secrets (database name, username, password)
  add_thing_to_service "secrets" "$DATABASE_SECRET" "services.database"
  add_thing_to_service "secrets" "$DATABASE_SECRET;external" "$BASE_PATH"
  # Attach to Network 'database'
  add_thing_to_service "networks" "database" services.database
  add_thing_to_service "networks" "database" "$BASE_PATH"
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
  cat "$TMP_YML" >"$1"
  echo "### Wrote yml file successfully."
fi

rm -f "$TMP_YML"
