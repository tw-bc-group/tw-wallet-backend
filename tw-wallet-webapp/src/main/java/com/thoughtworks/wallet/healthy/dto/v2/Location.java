package com.thoughtworks.wallet.healthy.dto.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class Location{
    @JsonProperty(value = "@type")
    private String          type;
    private String            name;
    private String            url;
}