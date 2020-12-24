package com.thoughtworks.wallet.healthy.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VcType {
    Integer id;
    String name;
    Integer issuerId;
    List<String> content;
}
