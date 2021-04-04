#!/bin/bash
set -e
cd ..

## Input ENV VARS
APPNAME="ucc-hello-world-api"
NAMESPACE="unity"
S3CHARTS="showme"


## Get current version from scala version.sbt file
cat version.sbt
echo
SEMVER=`cat version.sbt | grep "version :=" | sed  -n 's/[^0-9]*\([0-9\.]\+\)[^0-9]*/\1/p'`
FULVER=`cat version.sbt | grep "version :=" | sed  -n 's/[^0-9]*\([0-9A-Z\.\-]\+\)[^0-9]*/\1/p'`
echo "SEMVER: ${SEMVER}, FULVER: ${FULVER}"

## Update version in helm files
cd ./k8s/helm
sed -i "s/^version:.*$/version: ${FULVER}/" $APPNAME/Chart.yaml
sed -i "s/^appVersion:.*$/appVersion: ${FULVER}/" $APPNAME/Chart.yaml

echo "helm versions:"
cat $APPNAME/Chart.yaml | grep "ersion:"

## Generate pakage and render template from helm chart
mkdir -p ./target/
helm package $APPNAME 
helm template hello-world ./$APPNAME  --namespace=$NAMESPACE  > ./target/$APPNAME-$FULVER.yaml 


## upload chart to repo
AWSTEST=`aws sts get-caller-identity || exit 0`   #Check if awscli has valid credentials

if [ ! -z "$AWSTEST" ]
then

    # add repo
    helm repo add my-charts s3://$S3CHARTS/charts
    helm repo list

    # push chart
    helm s3 push --force "./$APPNAME-$FULVER.tgz" my-charts

fi


## To Artifact folder
mv *.tgz ./target/

echo "Result:"
ls -l ./target/


