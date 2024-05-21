package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryDuplicatedException extends ArtistryException{
    public ArtistryDuplicatedException() {super();}

    public ArtistryDuplicatedException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }

}
