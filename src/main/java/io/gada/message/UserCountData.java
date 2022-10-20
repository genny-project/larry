package io.gada.message;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserCountData {
    public String userCode;
    public long count;

    public UserCountData(String userCode, long count) {
        this.userCode = userCode;
        this.count = count;
    }
}
