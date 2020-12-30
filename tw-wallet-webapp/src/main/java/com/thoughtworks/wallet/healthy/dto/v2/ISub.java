package com.thoughtworks.wallet.healthy.dto.v2;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NoArgsConstructor;

@NoArgsConstructor
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonDeserialize(as=HealthySub.class)
public abstract class ISub {
 public abstract String  getId();
}
