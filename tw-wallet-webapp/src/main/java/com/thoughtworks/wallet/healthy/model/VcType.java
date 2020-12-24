package com.thoughtworks.wallet.healthy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class VcType {
    Integer id;
    String name;
    Integer issuerId;
    List<String> content;
}
