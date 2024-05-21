package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class TagNotFoundException extends ArtistryException{
    public TagNotFoundException() {super();}

    public TagNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }

}
