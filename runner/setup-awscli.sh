#!/bin/bash
set -e

# Requirements
sudo apt-get -y install python3-pip
pip3 install awscli

#check config
aws sts get-caller-identity || exit 0
