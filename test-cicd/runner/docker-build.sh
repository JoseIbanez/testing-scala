#!/bin/bash
set -e
DOCKER_IMAGE="ibanez/test-cicd"
DOCKER_VERSION="0.0.1-rc00000"

cd ..

#Build the image
sbt docker:publishLocal
docker images

#Upload to docker register hub
if [ -v DOCKER_USER ] && [ -v DOCKER_PASS ]
then
   echo $DOCKER_PASS | docker login --username $DOCKER_USER --password-stdin
   docker push $DOCKER_IMAGE:dev-latest
   docker push $DOCKER_IMAGE:$TAG
fi

