#!/bin/bash

NAME="sample"

ECR_ADDR="446804614856.dkr.ecr.ap-northeast-2.amazonaws.com"
REGISTRY="11st-registry"
TAG="sample-spring-v0.0.2"

CHARTMUSEUM="chartmuseum"

### maven build

# mvn clean
# mvn install


### docker build

docker build -t "${REGISTRY}:${TAG}" .
docker tag "${REGISTRY}:${TAG}" "${ECR_ADDR}/${REGISTRY}:${TAG}"

docker push "${ECR_ADDR}/${REGISTRY}:${TAG}"

### helm chart build
helm push charts chartmuseum

### helm deploy
helm repo update
helm upgrade --install "${NAME}" "${CHARTMUSEUM}/11st-backoffice"

