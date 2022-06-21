#!/usr/bin/env bash
declare -r SSH_HOME="${SSH_HOME:-"/home/$(whoami)/.ssh"}"
declare -r SSH_CONFIG="${SSH_CONFIG:-"$SSH_HOME/config"}"
declare -r SSH_HOST="${SSH_HOST:?"Missing SSH_HOST variable"}"
declare -r HOSTNAME="${HOSTNAME:?"Missing HOSTNAME variable"}"
declare -r SSH_USER="${SSH_USER:?"Missing SSH_USER variable"}"
declare -r KEY_FILE_SSH_VAR_NAME="${KEY_FILE_SSH_VAR_NAME:?"Missing KEY_FILE_SSH_VAR_NAME variable"}"
mkdir -p "$SSH_HOME"
if [[ -z "${!KEY_FILE_SSH_VAR_NAME}" ]]; then
  echo "Variable KEY_FILE_SSH_VAR_NAME point to an invalid variable '$KEY_FILE_SSH_VAR_NAME'..."
  exit 1
fi
echo "Setting SSH Config File for $SSH_USER@$HOSTNAME using $KEY_FILE_SSH_VAR_NAME"
declare -r first_line="Host $SSH_HOST"
if grep -q "$first_line" "$SSH_CONFIG"; then
  #   sed -i '/'"$first_line"'/,+5 d' "$SSH_CONFIG"
  echo "We find twice the same entry in ssh config file. This shouldn't happen since we are generating a new UUID random configi file each time."
  exit 1
fi
{
  echo "$first_line"
  echo "  User $SSH_USER"
  echo "  HostName $HOSTNAME"
  echo "  IdentityFile ${!KEY_FILE_SSH_VAR_NAME}"
  echo "  StrictHostKeyChecking=no"
  echo "  UserKnownHostsFile=/dev/null"
} >>"$SSH_CONFIG"
