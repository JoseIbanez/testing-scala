#!/bin/bash
set -e

# Requirements
#apt-get install -y openjdk-11-jre-headless
apt-get update -y
apt-get install -y sudo

curl https://baltocdn.com/helm/signing.asc | sudo apt-key add -
sudo apt-get install apt-transport-https --yes
echo "deb https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update -y
sudo apt-get install -y helm
