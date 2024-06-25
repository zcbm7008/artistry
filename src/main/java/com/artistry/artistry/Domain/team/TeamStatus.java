package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Exceptions.InvalidTeamStatusException;

import java.util.Arrays;

public enum TeamStatus {
    RECRUITING,
    CANCELED,
    FINISHED
    ;

    public static TeamStatus of(String target){
        return Arrays.stream(TeamStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(target))
                .findAny()
                .orElseThrow(() -> new InvalidTeamStatusException(target));
    }

}
