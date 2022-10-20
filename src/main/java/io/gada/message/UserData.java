package io.gada.message;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserData {
    public String userCode;
    public String jti;

    public UserData(String userCode, String jti) {
        this.userCode = userCode;
        this.jti = jti;
    }
}
