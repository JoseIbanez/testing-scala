#!/bin/bash
set -e
cd ..

sbt "runMain com.vodafone.ucc.middleware.accountcreation.Akka2KafkaProducerTest"
