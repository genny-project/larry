package io.gada.message;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserMessage {
    public int id;
    public String userCodeAccess;
    public String bridges;
    public String browserAgent;
    public int numLogins;
    public String active;
    public String jti;
    public String productCode;
    public String realm;

    public UserMessage() {
    }

    public UserMessage(String userCodeAccess, String jti){
        this.userCodeAccess = userCodeAccess;
        this.jti = jti;
    }

    public UserMessage(String userCodeAccess, String bridges, String browserAgent, int numLogins, String active, String jti,
                       String productCode){
        this.userCodeAccess = userCodeAccess;
        this.bridges = bridges;
        this.browserAgent = browserAgent;
        this.numLogins = numLogins;
        this.active = active;
        this.jti = jti;
        this.productCode = productCode;
    }
}
