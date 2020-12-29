package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class PassportSub implements ISub {
    // 持有者 id
    private String id;
    private String yourName;
    private String yourFamilyName;
    private String country;
}
