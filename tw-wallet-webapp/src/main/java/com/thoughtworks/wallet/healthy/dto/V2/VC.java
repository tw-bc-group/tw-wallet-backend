package com.thoughtworks.wallet.healthy.dto.V2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class VC {
    @JsonProperty(value = "@context")
    private List<String> context;
    private List<String> typ;
    // 证书 ID
    private String id;
    private Issuer issuer;
    private String name;
    private String desc;
    private Sub sub;
}