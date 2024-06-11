package com.artistry.artistry.Domain.application;

import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Exceptions.InvalidApplicatoinStatusException;
import com.artistry.artistry.Exceptions.InvalidTeamStatusException;

import java.util.Arrays;

public enum ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED
    ;

    public static ApplicationStatus of(String target){
        return Arrays.stream(ApplicationStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(target))
                .findAny()
                .orElseThrow(() -> new InvalidApplicatoinStatusException(target));
    }
}
