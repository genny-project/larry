package io.gada;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.gada.message.UserMessage;
import io.gada.message.UserData;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.jboss.logging.Logger;
import java.lang.invoke.MethodHandles;

@ApplicationScoped
public class TopologyProducer {
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    public static final String USER_STORE = "UserStore";

    private static final String TOPIC_USER = "user";
    private static final String TOPIC_USER_ACCESS = "user-access";

    @Produces
    public Topology getTopologyUser() {

        final StreamsBuilder builder = new StreamsBuilder();

        final ObjectMapperSerde<UserData> userSerder = new ObjectMapperSerde<>(UserData.class);
        final ObjectMapperSerde<UserMessage> jtiSerder = new ObjectMapperSerde<>(UserMessage.class);

        final GlobalKTable<Integer, UserData> userTable = builder.globalTable(TOPIC_USER,
                                                                    Consumed.with(Serdes.Integer(), userSerder));

        final KStream<String, UserMessage> userMessage = builder.stream(TOPIC_USER_ACCESS,
                                                            Consumed.with(Serdes.String(), jtiSerder));

        log.info("==================getTopologyUser()-Begin==================");
        userMessage
                .map((key, value) -> KeyValue.pair(value.id, value))
                .join(userTable, (userCode, userCodeAccess) -> userCode, (userCodeAccess, userData) -> userData)
                .print(Printed.toSysOut());

        log.info("==================getTopologyUser()-End==================");

        return builder.build();
    }
}
