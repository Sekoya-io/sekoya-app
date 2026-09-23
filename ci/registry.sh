#! /bin/bash

set -e -o pipefail

source "$(dirname $0)/common.inc"

# space-separated from json
KUBERNETES_TAGS_SPACE=$( echo "${KUBERNETES_TAGS}" | jq -r '. | join(" ")' )
# bash array from space-separated
read -a KUBERNETES_TAGS_ARR <<< "${KUBERNETES_TAGS_SPACE}"

if [ "${#KUBERNETES_TAGS_ARR[@]}" -gt 0 -a -z "${KUBERNETES_REGISTRY}" ]; then
    echo -e "\e[31mKUBERNETES_REGISTRY needed\e[0m"
    exit 1
fi
if [ "${#KUBERNETES_TAGS_ARR[@]}" -gt 0 -a -z "${KUBERNETES_ARTIFACT_ID}" ]; then
    echo -e "\e[31mKUBERNETES_ARTIFACT_ID needed\e[0m"
    exit 1
fi
if [ "${#KUBERNETES_TAGS_ARR[@]}" -gt 0 -a -z "${REGISTRY_USERNAME}" ]; then
    echo -e "\e[31mREGISTRY_USERNAME needed\e[0m"
    exit 1
fi
if [ "${#KUBERNETES_TAGS_ARR[@]}" -gt 0 -a -z "${REGISTRY_PASSWORD}" ]; then
    echo -e "\e[31mREGISTRY_PASSWORD needed\e[0m"
    exit 1
fi

# Build/publish goal is performed on package stage so we can skip maven install
mvn -Dspring-boot.build-image.skip=false -Dspring-boot.build-image.publish=true -DskipTests clean package
CURRENT_VERSION=$( mvn help:evaluate -q -Dexpression=project.version -DforceStdout )

if [ "${#KUBERNETES_TAGS_ARR[@]}" -eq 0 ]; then
    exit 0;
fi

echo "${REGISTRY_PASSWORD}" | docker login -u "${REGISTRY_USERNAME}" --password-stdin "${KUBERNETES_REGISTRY}"

for tag in ${KUBERNETES_TAGS_ARR[@]}; do
    echo "Add tag: ${KUBERNETES_REGISTRY}/${KUBERNETES_ARTIFACT_ID}:${tag}"
    docker tag "${KUBERNETES_REGISTRY}/${KUBERNETES_ARTIFACT_ID}:${CURRENT_VERSION}" "${KUBERNETES_REGISTRY}/${KUBERNETES_ARTIFACT_ID}:${tag}"
    docker push "${KUBERNETES_REGISTRY}/${KUBERNETES_ARTIFACT_ID}:${tag}"
done