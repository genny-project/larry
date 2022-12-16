package io.gada;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import io.gada.message.UserDataAgg;
import io.gada.message.source.Event;
import life.genny.qwandaq.data.GennyCache;
import life.genny.qwandaq.serialization.userstore.UserStore;
import life.genny.qwandaq.utils.CacheUtils;
import life.genny.qwandaq.utils.UserStoreUtils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.KTable;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
    public static final String TOPIC_OUTPUT = "users_agg";
    @ConfigProperty(name = "windows.seconds", defaultValue = "10")
    public int windowsSeconds;

    @Inject
    GennyCache cache;

    @Produces
    public Topology getTopologyUser() {
        CacheUtils.init(cache);

        final StreamsBuilder builder = new StreamsBuilder();
        Serde<String> stringSerde = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        final ObjectMapperSerde<Event> eventSerder = new ObjectMapperSerde<>(Event.class);
        final ObjectMapperSerde<UserStore> accessedSerder = new ObjectMapperSerde<>(UserStore.class);

//        final ObjectMapperSerde<UserLog> accessedSerder = new ObjectMapperSerde<>(UserLog.class);
//        final ObjectMapperSerde<UserData> userSerder = new ObjectMapperSerde<>(UserData.class);
//        final ObjectMapperSerde<UserDataAgg> aggSerder = new ObjectMapperSerde<>(UserDataAgg.class);
//        final GlobalKTable<String, UserData> userTable = builder.globalTable(TABLE_USER,
//                                                                    Consumed.with(stringSerde, userSerder));

        final KStream<String, Event> userEvents = builder.stream(TOPIC_EVENTS,Consumed.with(stringSerde, eventSerder));

        final KStream<UserStore, UserStore> sourceEvents = userEvents
                .map((k, v) -> KeyValue.pair(new UserStore(v.getRealm(),v.data.sourceCode,v.getJtiByToken()),new UserStore(v.getRealm(),k,v.getJtiByToken())))
                .filter((k, v)-> k.getUsercode() != null);

        KTable<Windowed<UserStore>,Long> agg = sourceEvents.groupByKey(Grouped.with(accessedSerder,accessedSerder))
                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(windowsSeconds)))
                .count();

        UserStoreUtils userStoreUtils = new UserStoreUtils();
        agg.toStream().mapValues((k,v) -> new UserDataAgg(k.key().getRealm(),k.key().getUsercode(),v))
                .foreach( (k,v)-> {
                    try {
                        log.info(k.key().getRealm() + ":" + k.key().getUsercode() + ":" + k.key().getJtiAccess() + ":" + k.key().getLastActive() + ":" + v.count);
//                        CacheUtils.putObject(k.key().getRealm(), USER_STORE + ":" + k.key().getUsercode(), k.key());
                        userStoreUtils.updateSerializableUserStore(k.key());
                    }catch (Exception ex) {
                        log.error(ex);
                        ex.printStackTrace();
                    }
                });

        return builder.build();
    }
}
