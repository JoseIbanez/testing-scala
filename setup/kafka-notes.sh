#!/bin/bash

##############
# Checking kafka events
###############


cd $HOME/downloads/kafka_*/

bin/kafka-console-producer.sh --topic license --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic license --from-beginning --bootstrap-server localhost:9092



## Docker mode
## Status & Logs

docker run -it \
    --network app-tier \
    wurstmeister/kafka \
    /bin/sh
cd /opt/kafka
./bin/kafka-topics.sh --list --zookeeper zookeeper-server:2181
./bin/kafka-console-consumer.sh --topic license          --from-beginning --bootstrap-server kafka-server:9092
./bin/kafka-console-consumer.sh --topic appDirectEvent   --from-beginning --bootstrap-server kafka-server:9092
./bin/kafka-console-consumer.sh --topic ringCentralOrder --from-beginning --bootstrap-server kafka-server:9092

./bin/kafka-create-topic.sh --zookeeper zookeeper-server:2181 --partition 2 --topic appDirectEvent
./bin/kafka-topics.sh --zookeeper zookeeper-server:2181 --alter --topic appDirectEvent  --partitions 10
./bin/kafka-topics.sh --zookeeper zookeeper-server:2181 --describe --topic appDirectEvent
./bin/kafka-run-class.sh kafka.tools.ConsumerOffsetChecker  --topic appDirectEvent --zookeeper zookeeper-server:2181 --group group-1



###########################
# Run kafka in command line
###########################

# Target folder
mkdir -p $HOME/downloads
cd $HOME/downloads

# Download
#wget "https://ftp.cixug.es/apache/kafka/2.6.0/kafka_2.13-2.6.0.tgz"
wget "https://ftp.cixug.es/apache/kafka/2.7.0/kafka_2.13-2.7.0.tgz"

tar -xzf kafka_2*.tgz
cd kafka_2*/

#Run in backgroup with screen
screen -d -m -S zoo \
./bin/zookeeper-server-start.sh config/zookeeper.properties

screen -d -m -S kafka \
./bin/kafka-server-start.sh config/server.properties


