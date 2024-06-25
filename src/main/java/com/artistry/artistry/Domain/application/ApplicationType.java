
package com.artistry.artistry.Domain.application;

import com.artistry.artistry.Exceptions.InvalidApplicatoinStatusException;

import java.util.Arrays;

public enum ApplicationType {

    APPLICATION,
    INVITATION
    ;

    public static ApplicationType of(String target){
        return Arrays.stream(ApplicationType.values())
                .filter(status -> status.name().equalsIgnoreCase(target))
                .findAny()
                .orElseThrow(() -> new InvalidApplicatoinStatusException(target));
    }
}
