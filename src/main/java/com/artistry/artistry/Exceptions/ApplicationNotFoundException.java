package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ApplicationNotFoundException extends ArtistryException{

    public ApplicationNotFoundException() {super();}

    public ApplicationNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
