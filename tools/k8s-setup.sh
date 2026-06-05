#! /bin/bash

set -e -o pipefail

rbw unlock

KUBE_CLUSTER_NAME=sekoya-kube
KUBE_CLUSTER_CERTIFICATE_AUTHORITY_DATA="$( rbw get -f password Sekoya/k8s/server )"
KUBE_CLUSTER_SERVER="$( rbw get -f username "Sekoya/k8s/server" )"
KUBE_CREDENTIAL_TOKEN="$( rbw get -f password "Sekoya/k8s/$USER" )"
KUBE_NAMESPACE=qualification

kubectl config set "clusters.${KUBE_CLUSTER_NAME}.certificate-authority-data" "${KUBE_CLUSTER_CERTIFICATE_AUTHORITY_DATA}"
kubectl config set "clusters.${KUBE_CLUSTER_NAME}.server" "${KUBE_CLUSTER_SERVER}"
kubectl config set-credentials deployment --token "${KUBE_CREDENTIAL_TOKEN}"
kubectl config set-context "${USER}@${KUBE_CLUSTER_NAME}" --user=deployment "--namespace=${KUBE_NAMESPACE}" "--cluster=${KUBE_CLUSTER_NAME}"
kubectl config use-context "${USER}@${KUBE_CLUSTER_NAME}"

kubectl get pod