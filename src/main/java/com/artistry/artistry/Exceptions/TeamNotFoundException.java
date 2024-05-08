package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends ArtistryException{

    public TeamNotFoundException() {super();}

    public TeamNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
