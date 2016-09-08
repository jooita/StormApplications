#!/bin/sh

cd /pub/kafkaPerf
mvn exec:java -Dstorm.topology=keti.storm.kafka.topology.WordCountTopologyLocal -Dlog4j.configurationFile=/pub/kafkaPerf/log4j2.xml -Dexec.args="$1 -d"
