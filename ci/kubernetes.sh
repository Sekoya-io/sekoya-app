#! /bin/sh

set -e -o pipefail

source "$(dirname $0)/common.inc"

if [ "$STAGE_RESET" == "true" ]; then
    stage_enabled reset
else
    stage_disabled reset
fi

if [ -z "${KUBE_CLUSTER_NAME}" ]; then
    echo -e "\e[31mKUBE_CLUSTER_NAME needed\e[0m"
    exit 1
fi
if [ -z "${KUBE_CLUSTER_CERTIFICATE_AUTHORITY_DATA}" ]; then
    echo -e "\e[31mKUBE_CLUSTER_CERTIFICATE_AUTHORITY_DATA needed\e[0m"
    exit 1
fi
if [ -z "${KUBE_CLUSTER_SERVER}" ]; then
    echo -e "\e[31mKUBE_CLUSTER_SERVER needed\e[0m"
    exit 1
fi
if [ -z "${KUBE_CREDENTIAL_TOKEN}" ]; then
    echo -e "\e[31mKUBE_CREDENTIAL_TOKEN needed\e[0m"
    exit 1
fi
if [ -z "${KUBE_NAMESPACE}" ]; then
    echo -e "\e[31mKUBE_NAMESPACE needed\e[0m"
    exit 1
fi
if [ -z "${KUBE_DEPLOYMENT}" ]; then
    echo -e "\e[31mKUBE_DEPLOYMENT needed\e[0m"
    exit 1
fi
if [ "${STAGE_RESET}" == "true" -a -z "${KUBE_RESET_CRONJOB}" ]; then
    echo -e "\e[31mKUBE_RESET_CRONJOB needed\e[0m"
    exit 1
fi

section_start configuration Configuring kubernetes account...
kubectl config set "clusters.${KUBE_CLUSTER_NAME}.certificate-authority-data" "${KUBE_CLUSTER_CERTIFICATE_AUTHORITY_DATA}"
kubectl config set "clusters.${KUBE_CLUSTER_NAME}.server" "${KUBE_CLUSTER_SERVER}"
kubectl config set-credentials deployment --token "${KUBE_CREDENTIAL_TOKEN}"
kubectl config set-context "deployment@${KUBE_CLUSTER_NAME}" --user=deployment "--namespace=${KUBE_NAMESPACE}" "--cluster=${KUBE_CLUSTER_NAME}"
kubectl config use-context "deployment@${KUBE_CLUSTER_NAME}"
kubectl config get-clusters
kubectl config get-contexts
kubectl config get-users
kubectl get deployment/${KUBE_DEPLOYMENT}
section_end configuration

section_start deploy "Deploy last version on ${KUBE_NAMESPACE}"
job_name=${KUBE_RESET_CRONJOB}-$( date "+%Y%m%d-%H%M%S" )
kubectl "--context=deployment@${KUBE_CLUSTER_NAME}" -n "${KUBE_ENVIRONMENT}" scale deployment/${KUBE_DEPLOYMENT} --replicas=0
if [ "$STAGE_RESET" == "true" ]; then
    kubectl "--context=deployment@${KUBE_CLUSTER_NAME}" -n "${KUBE_ENVIRONMENT}" create job --from cronjob/${KUBE_RESET_CRONJOB} "${job_name}"
    kubectl "--context=deployment@${KUBE_CLUSTER_NAME}" -n "${KUBE_ENVIRONMENT}" wait --for=condition=complete "job/${job_name}"
fi
kubectl "--context=deployment@${KUBE_CLUSTER_NAME}" -n "${KUBE_ENVIRONMENT}" scale deployment/${KUBE_DEPLOYMENT} --replicas=1
section_end deploy

if [ "$STAGE_RESET" == "true" ]; then
    stage_success reset
else
    stage_skipped reset
fi
stage_success deploy
