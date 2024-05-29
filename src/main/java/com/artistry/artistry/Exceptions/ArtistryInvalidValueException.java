package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryInvalidValueException extends ArtistryException{
    public ArtistryInvalidValueException() {super();}

    public ArtistryInvalidValueException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.BAD_REQUEST; }
}
