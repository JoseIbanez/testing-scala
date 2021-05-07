#!/bin/bash 

docker network create app-tier --driver bridge

#######################
# Clean up

docker kill mongodb-server
docker rm mongodb-server

#######################
# Start the containers

docker run -d \
        --name mongodb-server \
        --network app-tier \
        -e MONGO_INITDB_ROOT_USERNAME=user1 \
        -e MONGO_INITDB_ROOT_PASSWORD=user1 \
        -p 27017-27019:27017-27019 mongo


sudo apt-get install -y mongodb-clients

