#!/bin/bash

# Setup Vagrant VM:
## jdk, stb, docker



# Pre-requirements
sudo apt-get install -y openjdk-11-jre-headless

# SBT install
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add
sudo apt-get update -y
sudo apt-get install -y sbt


# Docker.io
sudo apt-get install -y docker.io
sudo usermod -aG docker $USER


