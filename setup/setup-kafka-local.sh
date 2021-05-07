#!/bin/bash


docker network create app-tier --driver bridge

#######################
# Clean up

docker kill kafka-server
docker kill zookeeper-server
docker rm kafka-server
docker rm zookeeper-server

#######################
# Start the containers




docker run -d --name zookeeper-server \
    --network app-tier \
    -p 2181:2181 \
    wurstmeister/zookeeper

docker run -d --name kafka-server \
    --network app-tier \
    -p 9092:9092 \
    -v /var/run/docker.sock:/var/run/docker.sock  \
    -e KAFKA_ADVERTISED_HOST_NAME=kafka-server \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper-server:2181 \
    wurstmeister/kafka

sleep 10

docker logs kafka-server
