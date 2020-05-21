package com.thoughtworks.wallet.healthy.model;

import lombok.Getter;
import lombok.Setter;

public enum Result {
    YES("Yes"),
    NO("No"),
    NOT_SURE("NotSure");

    @Getter
    @Setter
    private String status;

    Result(String status) {
        this.status = status;
    }

    public static Result of(String result) {
        switch (result) {
            case "Yes":
                return YES;
            case "No":
                return NO;
            case "NotSure":
                return NOT_SURE;
        }
        return null;
    }
}
