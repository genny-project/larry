package io.gada.message.source;


import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Data {
    public String attributeCode;
    public String code;
    public String parentCode;
    public String processId;
    public String sourceCode;
    public String targetCode;

}
