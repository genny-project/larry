package io.gada.message.source;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;
import org.jose4j.base64url.Base64;

import java.lang.invoke.MethodHandles;

@RegisterForReflection
public class Event {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public Data data;
    public String token;
    public String jti;
    public String msg_type;
    public boolean redirect;
    public String realm;

    public Event(String jti, String realm) {
        this.jti = jti;
        this.realm = realm;
    }

    public String getJtiByToken() {
        String payload = new String(Base64.decode(token.split("\\.")[1]));
        JsonObject jsonPayload = new JsonObject(payload);
        String jti = jsonPayload.getString("jti");

        LOGGER.info("jti:" + jti);
        return jti;
    }

    public String getRealm() {
        String payload = new String(Base64.decode(token.split("\\.")[1]));
        JsonObject jsonPayload = new JsonObject(payload);

        String iss = jsonPayload.getString("iss");
        String[] deconstructBySlash = iss.split("/");
        String realm = deconstructBySlash[deconstructBySlash.length - 1];

        LOGGER.info("realm:" + realm);
        return realm;
    }
}
