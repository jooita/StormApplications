#!/bin/sh

cd /pub/kafkaPerf
#mvn exec:java -Dstorm.topology=keti.storm.kafka.topology.StormKafkaSimpleTopologyLocal -Dlog4j.configurationFile=/pub/kafkaPerf/log4j2.xml -Dexec.args="$1"
mvn exec:java -Dstorm.topology=keti.storm.kafka.topology.StormKafkaSimpleTopologyLocal -Dexec.args="$1"
