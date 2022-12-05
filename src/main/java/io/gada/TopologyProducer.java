package io.gada;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.gada.message.UserDataAgg;
import io.gada.message.processed.UserLog;
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

    public static final String TABLE_USER = "users";
    public static final String TOPIC_EVENTS = "events";
    public static final String TOPIC_DATA = "data";
//    private static final String TOPIC_OUTPUT = "users_agg";
    public static final String TOPIC_OUTPUT = "users_agg";
    public static final int WINDOWS_SECONDS = 10;

    @Produces
    public Topology getTopologyUser() {

        final StreamsBuilder builder = new StreamsBuilder();
        Serde<String> stringSerde = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        final ObjectMapperSerde<Event> eventSerder = new ObjectMapperSerde<>(Event.class);
        final ObjectMapperSerde<UserData> userSerder = new ObjectMapperSerde<>(UserData.class);
        final ObjectMapperSerde<UserLog> accessedSerder = new ObjectMapperSerde<>(UserLog.class);
        final ObjectMapperSerde<UserDataAgg> aggSerder = new ObjectMapperSerde<>(UserDataAgg.class);

        final GlobalKTable<String, UserData> userTable = builder.globalTable(TABLE_USER,
                                                                    Consumed.with(stringSerde, userSerder));

        final KStream<String, Event> userEvents = builder.stream(TOPIC_EVENTS,
                                                                    Consumed.with(stringSerde, eventSerder));

//        userEvents.print(Printed.toSysOut());

        final KStream<UserLog, UserLog> sourceEvents = userEvents
                .map((k, v) -> KeyValue.pair(new UserLog(k,v.getJtiByToken(),v.getRealm()),new UserLog(k,v.getJtiByToken(),v.getRealm())))
                .filter((k, v)-> k != null);

        KTable<Windowed<UserLog>,Long> agg = sourceEvents.groupByKey(Grouped.with(accessedSerder,accessedSerder))
                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(WINDOWS_SECONDS)))
                .count();

        agg.toStream().print(Printed.toSysOut());

//      agg.toStream().mapValues((k,v) -> new UserDataAgg(k.key().realm,k.key().realm,v))
//                .to(TOPIC_OUTPUT);

        agg.toStream().mapValues((k,v) -> new UserDataAgg(k.key().realm,k.key().userCode,v))
                    .foreach( (k,v)-> System.out.println(k.key().realm + ":" + k.key().userCode));


        /*
//        agg.toStream().print(Printed.toSysOut());
//        agg.toStream().map((k,v) -> KeyValue.pair(k,v)).to(TOPIC_OUTPUT);
//        agg.toStream().to(TOPIC_OUTPUT,Produced.with(accessedSerder,longSerde));
//        agg.toStream().to(TOPIC_OUTPUT, Produced.with(accessedSerder,Serdes.Long()));

        //CacheUtils.putObject();
        */

        return builder.build();
    }
}
