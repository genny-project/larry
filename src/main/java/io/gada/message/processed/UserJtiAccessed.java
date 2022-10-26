package io.gada.message.processed;

import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;
import org.jose4j.base64url.Base64;

import java.lang.invoke.MethodHandles;

public class UserJtiAccessed implements java.io.Serializable, Comparable<UserJtiAccessed>{
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public String userCode;
    public String jtiAccess;
    public String realm;

    public UserJtiAccessed() {
    }

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


    @Override
    public int compareTo(UserJtiAccessed o) {
        if(this.realm.equals(o.realm) && this.userCode.equals(o.userCode)
                && this.jtiAccess.equals(o.jtiAccess)) {
            return 0;
        }
        
        return -1;
    }
}
