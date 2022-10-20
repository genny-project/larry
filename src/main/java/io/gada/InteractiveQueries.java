package io.gada;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.gada.message.UserCountData;
import io.gada.message.UserCountData;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;

import java.util.Optional;

@ApplicationScoped
public class InteractiveQueries {

    @Inject
    KafkaStreams streams;

    public Optional<UserCountData> getUserAccessCountData(int id) {
        UserCountData moviePlayCount = getUserCount().get(id);

        if (moviePlayCount != null) {
            return Optional.of(new UserCountData(moviePlayCount.userCode, moviePlayCount.count));
        } else {
            return Optional.empty();
        }
    }

    private ReadOnlyKeyValueStore<Integer, UserCountData> getUserCount() {
        while (true) {
            try {
                return streams.store(fromNameAndType(TopologyProducer.USER_STORE, QueryableStoreTypes.keyValueStore()));
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }

}
