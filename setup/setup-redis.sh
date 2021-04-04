#!/bin/bash


#######################
# Clean up

docker kill redis-server
docker rm redis-server

#######################
# Star container
docker run -d --name redis-server \
    --network app-tier \
    -p 6379:6379 \
    redis


## Redis client
sudo apt-get install -y redis-tools


