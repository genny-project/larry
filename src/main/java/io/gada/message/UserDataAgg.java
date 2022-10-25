package io.gada.message;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.logging.Logger;

import java.lang.invoke.MethodHandles;

@RegisterForReflection
public class UserDataAgg {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public String userCode;
    public String jti;
    public String realm;
    public long count;

    public UserDataAgg() {
    }

    public UserDataAgg(String userCode) {
        this.userCode = userCode;
    }

    public UserDataAgg(String userCode,String jti, String realm) {
        this.userCode = userCode;
        this.jti = jti;
        this.realm = realm;
    }

    public UserDataAgg(String userCode, long count) {
        this.userCode = userCode;
        this.count = count;
    }
}
