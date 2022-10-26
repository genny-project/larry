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
    private static final String TOPIC_OUTPUT = "users_agg";
    private static final int WINDOWS_SECONDS = 10;

    @Produces
    public Topology getTopologyUser() {

        final StreamsBuilder builder = new StreamsBuilder();
        Serde<String> stringSerde = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        final ObjectMapperSerde<Event> eventSerder = new ObjectMapperSerde<>(Event.class);
        final ObjectMapperSerde<UserData> userSerder = new ObjectMapperSerde<>(UserData.class);
        final ObjectMapperSerde<UserJtiAccessed> accessedSerder = new ObjectMapperSerde<>(UserJtiAccessed.class);
        final ObjectMapperSerde<UserDataAgg> aggSerder = new ObjectMapperSerde<>(UserDataAgg.class);

        final GlobalKTable<String, UserData> userTable = builder.globalTable(TABLE_USER,
                                                                    Consumed.with(Serdes.String(), userSerder));

        final KStream<String, Event> userEvents = builder.stream(TOPIC_EVENTS,
                                                                    Consumed.with(Serdes.String(), eventSerder));

        final KStream<UserJtiAccessed, UserJtiAccessed> sourceEvents = userEvents
                .map((k, v) -> KeyValue.pair(new UserJtiAccessed(k,v.getJtiByToken(),v.getRealm()),new UserJtiAccessed(k,v.getJtiByToken(),v.getRealm())))
                .filter((k, v)-> k != null);

//        sourceEvents.print(Printed.toSysOut());

        KTable<Windowed<UserJtiAccessed>,Long> agg = sourceEvents.groupByKey(Grouped.with(accessedSerder,accessedSerder))
                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(WINDOWS_SECONDS)))
                .count();

//        agg.toStream().print(Printed.toSysOut());
        agg.toStream().to(TOPIC_OUTPUT);

//        agg.toStream().to(TOPIC_OUTPUT,Produced.with(accessedSerder,longSerde));
//        agg.toStream().to(TOPIC_OUTPUT, Produced.with(accessedSerder,Serdes.Long()));

//        agg.toStream().to(TOPIC_OUTPUT);
//        agg.toStream().foreach((k,v)-> {
//            System.out.println(k);
//            System.out.println(v);
//        });

//        agg.toStream().to(TOPIC_OUTPUT, Produced..with(stringSerde,longSerde));
//        agg.toStream().to(TOPIC_OUTPUT, Produced.as("processor1"));
//                .print(Printed.toSysOut());


        //CacheUtils.putObject();

        return builder.build();
    }
}
