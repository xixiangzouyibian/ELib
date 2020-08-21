package com.martin.ELib.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MethodDetails {

    private String methodName;
    private String methodArgs;
    private long excutionTime;
}
