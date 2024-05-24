package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryLengthException extends ArtistryException{
    public ArtistryLengthException() {super();}

    public ArtistryLengthException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.BAD_REQUEST; }
}
