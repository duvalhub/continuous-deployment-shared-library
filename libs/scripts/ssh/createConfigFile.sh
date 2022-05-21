#!/usr/bin/env bash
declare -r SSH_HOME="${SSH_HOME:-"/home/$(whoami)/.ssh"}"
declare -r SSH_CONFIG="${SSH_CONFIG:-"$SSH_HOME/config"}"
declare -r HOST="${HOST:?"Missing HOST variable"}"
declare -r SSH_USER="${SSH_USER:?"Missing SSH_USER variable"}"
declare -r KEY_FILE_SSH_VAR_NAME="${KEY_FILE_SSH_VAR_NAME:?"Missing KEY_FILE_SSH_VAR_NAME variable"}"
mkdir -p "$SSH_HOME"
if [[ -z "${!KEY_FILE_SSH_VAR_NAME}" ]]; then
  echo "Variable KEY_FILE_SSH_VAR_NAME point to an invalid variable '$KEY_FILE_SSH_VAR_NAME'..."
  exit 1
fi
echo "Setting SSH Config File for $SSH_USER@$HOST using $KEY_FILE_SSH_VAR_NAME"
{
  echo "Host $HOST"
  echo "  User $SSH_USER"
  echo "  HostName $HOST"
  echo "  IdentityFile ${!KEY_FILE_SSH_VAR_NAME}"
  echo "  StrictHostKeyChecking=no"
  echo "  UserKnownHostsFile=/dev/null"
} >> "$SSH_CONFIG"
