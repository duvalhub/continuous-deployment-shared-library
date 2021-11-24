#!/usr/bin/env sh
set -e

ENV_VARIABLE=""

if [ ! -z "$APPLICATION_PROFILE" ]
then
	ENV_VARIABLE+=" -Dspring.profiles.active=$APPLICATION_PROFILE"
fi

java \
-Djava.security.egd=file:/dev/./urandom \
$ENV_VARIABLE \
-jar "$JAR_FILE"
