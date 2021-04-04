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
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    -e ZOOKEEPER_TICK_TIME=2000 \
    confluentinc/cp-zookeeper



docker run -d --name kafka-server \
    --network app-tier \
    -p 9092:9092 \
    -e KAFKA_BROKER_ID=1 \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper-server:2181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server:9092 \
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT \
    -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
    -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
    confluentinc/cp-kafka


sleep 10

docker logs kafka-server
