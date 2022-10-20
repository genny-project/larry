package io.gada.profile;


import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class CustomTestProfile implements QuarkusTestProfile {

     @Override
     public Map<String, String> getConfigOverrides() {
        return Map.of("quarkus.kafka-streams.topics", "user,user-access");
    }


}