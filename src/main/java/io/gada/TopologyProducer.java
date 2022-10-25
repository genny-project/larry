package io.gada;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.gada.message.UserDataAgg;
import io.gada.message.processed.UserJtiAccessed;
import io.gada.message.source.Event;
import io.gada.message.UserData;
//import life.genny.qwandaq.utils.CacheUtils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.jboss.logging.Logger;
import java.lang.invoke.MethodHandles;
import java.time.Duration;

@ApplicationScoped
public class TopologyProducer {
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    public static final String USER_STORE = "UserStore";

    private static final String TABLE_USER = "users";
    private static final String TOPIC_EVENTS = "events";
    private static final String TOPIC_DATA = "data";

    @Produces
    public Topology getTopologyUser() {

        final StreamsBuilder builder = new StreamsBuilder();

        final ObjectMapperSerde<Event> eventSerder = new ObjectMapperSerde<>(Event.class);
        final ObjectMapperSerde<UserData> userSerder = new ObjectMapperSerde<>(UserData.class);

        final GlobalKTable<String, UserData> userTable = builder.globalTable(TABLE_USER,
                                                                    Consumed.with(Serdes.String(), userSerder));

        final KStream<String, Event> userEvents = builder.stream(TOPIC_EVENTS,
                                                                    Consumed.with(Serdes.String(), eventSerder));

        log.info("==================getTopologyUser()-Begin==================");
        final KStream<String, UserJtiAccessed> sourceEvents = userEvents
                .map((k, v) -> KeyValue.pair(v.data.sourceCode,new UserJtiAccessed(k,v.getJtiByToken(),v.getRealm())))
                .filter((k, v)-> k != null);

//        final KStream<String,UserJtiAccessed> parsedEvents  = sourceEvents.map((k,v) ->
//                                KeyValue.pair(k, new UserJtiAccessed(k,v)));

        final KStream  joined = sourceEvents
                .join(userTable, (k1,v1) -> k1,(v1,v2) -> new UserDataAgg(v1.userCode,v1.jtiAccess, v1.realm));
//                .groupByKey()
//                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(120)));

       final KTable agg = joined.groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(10)))
                .count(Materialized.with(Serdes.String(),Serdes.String()));

        agg.toStream().print(Printed.toSysOut());

        //CacheUtils.putObject();

        log.info("==================getTopologyUser()-End==================");

        return builder.build();
    }
}
