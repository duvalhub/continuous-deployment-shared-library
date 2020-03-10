#!/bin/bash
set -e

ENV_VARIABLE=""

if [ ! -z $APPLICATION_PROFILE ]
then
	ENV_VARIABLE+=" -Dspring.profiles.active=$APPLICATION_PROFILE"
fi

/usr/bin/java \
-Djava.security.egd=file:/dev/./urandom \
$ENV_VARIABLE \
-jar app.jar