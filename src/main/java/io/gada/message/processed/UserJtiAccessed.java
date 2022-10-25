package io.gada.message.processed;

import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;
import org.jose4j.base64url.Base64;

import java.lang.invoke.MethodHandles;

public class UserJtiAccessed {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public String userCode;
    public String jtiAccess;
    public String realm;

    public UserJtiAccessed(String jtiAccess) {
        this.jtiAccess = jtiAccess;
    }

    public UserJtiAccessed(String userCode, String jtiAccess) {
        this.userCode = userCode;
        this.jtiAccess = jtiAccess;
    }

    public UserJtiAccessed(String userCode, String jtiAccess,String realm) {
        this.userCode = userCode;
        this.jtiAccess = jtiAccess;
        this.realm = realm;
    }

    public String getJtiByToken(String token) {
        String payload = new String(Base64.decode(token.split("\\.")[1]));
        JsonObject jsonPayload = new JsonObject(payload);
        String jti = jsonPayload.getString("jti");

        LOGGER.info("===============getJti()===================" + jti);
        return jti;
    }

    public String getRealm(String token) {
        String payload = new String(Base64.decode(token.split("\\.")[1]));
        JsonObject jsonPayload = new JsonObject(payload);

        String iss = jsonPayload.getString("iss");
        String[] deconstructBySlash = iss.split("/");
        String realm = deconstructBySlash[deconstructBySlash.length - 1];

        LOGGER.info("===============getJti()===================" + realm);
        return realm;
    }
}
