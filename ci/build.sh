#! /bin/bash

set -e -o pipefail

source "$(dirname $0)/common.inc"

if [ "$STAGE_SPOTLESS" == "true" ]; then
    stage_enabled spotless
else
    stage_disabled spotless
fi
if [ "$STAGE_TEST" == "true" ]; then
    MAVEN_OPTS="${MAVEN_OPTS} -DskipTests=false"
    MAVEN_ARGS="${MAVEN_ARGS} -fae"
    stage_enabled test
else
    MAVEN_OPTS="${MAVEN_OPTS} -DskipTests=true"
    stage_disabled test
fi
if [ "$STAGE_IMAGE" == "true" ]; then
    MAVEN_OPTS="${MAVEN_OPTS} -Dspring-boot.build-image.skip=false"
    stage_enabled image
else
    MAVEN_OPTS="${MAVEN_OPTS} -Dspring-boot.build-image.skip=true"
    stage_disabled image
fi
if [ "$STAGE_PUBLISH" == "true" ]; then
    MAVEN_OPTS="${MAVEN_OPTS} -Dspring-boot.build-image.publish=true"
    stage_enabled publish
else
    MAVEN_OPTS="${MAVEN_OPTS} -Dspring-boot.build-image.publish=false"
    stage_disabled publish
fi

if [ "${STAGE_PUBLISH}" == "true" ]; then
    if [ -z "${REGISTRY_USERNAME}" ]; then
    echo -e "\e[31mREGISTRY_USERNAME needed\e[0m"
    exit 1
    fi
    if [ -z "${REGISTRY_PASSWORD}" ]; then
    echo -e "\e[31mREGISTRY_PASSWORD needed\e[0m"
    exit 1
    fi
fi

if [ "${STAGE_SPOTLESS}" == "true" ]; then
    section_start spotless Checking spotless format rules...
    if mvn spotless:check; then
        section_end spotless
        stage_success spotless
    else
        section_end spotless
        stage_failed spotless
    fi
else
    stage_skipped spotless
fi

echo -e "Maven options: \e[34m${MAVEN_OPTS}\e[0m"

section_start build Running maven build...
MAVEN_ARGS="${MAVEN_ARGS}" MAVEN_OPTS="${MAVEN_OPTS}" mvn -U verify
section_end build


if [ "$STAGE_TEST" == "true" ]; then
    stage_success test
else
    stage_skipped test
fi
if [ "$STAGE_IMAGE" == "true" ]; then
    stage_success image
else
    stage_skipped image
fi
if [ "$STAGE_PUBLISH" == "true" ]; then
    stage_success publish
else
    stage_skipped publish
fi