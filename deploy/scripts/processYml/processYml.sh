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
add_external_network() {
    local network_ref="${1:-internal}"
    local network_name="${2:-$network_ref}"
    yq w -i "$TMP_YML" "networks.$network_ref.name" $network_name
    yq w -i "$TMP_YML" "networks.$network_ref.external" "true"
}
add_volume() {
    local IFS=':'
    read -ra VOL <<< "$1"
    local name="${VOL[0]}"
    local external="${VOL[2]}"
    local base_path=""
    if [ ! -z "$2" ]; then
        base_path="$2.volumes"
    else
        base_path="volumes"
    fi
    yq w -i "$TMP_YML" "$base_path.$name.name" $name
    if [ ! -z "$external" ]; then
        yq w -i "$TMP_YML" "volumes.$name.external" "true"
    fi
}
add_volume_to_service() {
    local IFS=':'
    read -ra VOL <<< "$1"
    local name="${VOL[0]}"
    local mount_point="${VOL[1]}"
    local base_path="$2"
    yq w -i "$TMP_YML" "$base_path.volumes[+]" "$name:$mount_point"
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
# Networks Section
add_external_network internal "$STACK_NAME"_internal
if [ ! -z "$HOSTS" ]; then
    add_external_network reverseproxy
fi


#####################
# Volumes Section
if [ ! -z "$VOLUMES" ]; then
    for volume in $VOLUMES; do
        add_volume "$volume"
    done
fi

#####################
# App Section
# Image
yq w -i "$TMP_YML" "$BASE_PATH.image" "$IMAGE"

# Environments
if [ ! -z "$PORT" ];
then
    yq w -i "$TMP_YML" "$BASE_PATH.environment[+]" "VIRTUAL_PORT=$PORT"
fi

if [ ! -z "$HOSTS" ]; then
    yq w -i "$TMP_YML" "$BASE_PATH.environment[+]" "VIRTUAL_HOST=$HOSTS"
    yq w -i "$TMP_YML" "$BASE_PATH.environment[+]" "LETSENCRYPT_HOST=$HOSTS"
fi

# Networks
yq w -i "$TMP_YML" "$BASE_PATH.networks[+]" internal
if [ ! -z "$HOSTS" ]; then
    yq w -i "$TMP_YML" "$BASE_PATH.networks[+]" reverseproxy
fi

# Volumes
if [ ! -z "$VOLUMES" ]; then
    for volume in $VOLUMES; do
        add_volume_to_service "$volume" "$BASE_PATH"
    done
fi

echo "### Result : "
cat "$TMP_YML"

if [ ! -z "$1" ]; then
    cat "$TMP_YML" > "$1"
    echo "### Wrote yml file succesfully."
fi
