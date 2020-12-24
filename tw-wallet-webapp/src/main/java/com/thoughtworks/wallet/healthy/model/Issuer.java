package com.thoughtworks.wallet.healthy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class Issuer {
    Integer id;
    String name;
}
