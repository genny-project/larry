package io.gada.message.processed;

import org.jboss.logging.Logger;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

public class UserLog implements java.io.Serializable, Comparable<UserLog>{
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public String userCode;
    public String jtiAccess;
    public String realm;

    public long timestamp;

    public UserLog() {
    }

    public UserLog(String jtiAccess) {
        this.jtiAccess = jtiAccess;
    }

    public UserLog(String userCode, String jtiAccess) {
        this.userCode = userCode;
        this.jtiAccess = jtiAccess;
    }

    public UserLog(String userCode, String jtiAccess, String realm) {
        this.userCode = userCode;
        this.jtiAccess = jtiAccess;
        this.realm = realm;
        this.timestamp = Instant.now().getEpochSecond();
    }


    @Override
    public int compareTo(UserLog o) {
        if(this.realm.equals(o.realm) && this.userCode.equals(o.userCode)
                && this.jtiAccess.equals(o.jtiAccess)) {
            return 0;
        }
        
        return -1;
    }
}
