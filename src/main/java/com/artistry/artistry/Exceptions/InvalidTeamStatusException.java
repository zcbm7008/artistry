package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTeamStatusException extends ArtistryException{
    public InvalidTeamStatusException() {super();}

    public InvalidTeamStatusException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
