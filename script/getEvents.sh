export KAFKA_BIN=~/confluent-7.2.2/bin
export KAFKA_BROKERS=localhost:9092
export topic=events

#${KAFKA_BIN}/kafka-console-consumer --bootstrap-server ${KAFKA_BROKERS} --topic ${topic} --from-beginning --max-messages 100
${KAFKA_BIN}/kafka-console-consumer --bootstrap-server ${KAFKA_BROKERS} --topic ${topic}








