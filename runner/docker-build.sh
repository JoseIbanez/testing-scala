#!/bin/bash
set -e
cd ..

## Input ENV VARS from Gitlab
# DOCKER_ERC
# DOCKER_USER
# DOCKER_PASS

cat version.sbt

#Build the image
sbt clean docker:publishLocal
docker images


#TODO: Use better filter
#DOCKER_IMAGE=`docker images --filter "since=openjdk:11" --format "{{.Repository}}:{{.Tag}}" | head -n 1`

#Docker hub ERC
DOCKER_ERC=""

#Upload to docker register hub
if [ -v DOCKER_USER ] && [ -v DOCKER_PASS ]
then
   echo "Pushing IMAGE to ECR"
   echo $DOCKER_PASS | docker login --username $DOCKER_USER --password-stdin $DOCKER_ECR
   #docker push $DOCKER_IMAGE
   sbt docker:publish
fi

