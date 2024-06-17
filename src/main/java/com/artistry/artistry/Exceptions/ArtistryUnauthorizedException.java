package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryUnauthorizedException extends ArtistryException{
    public ArtistryUnauthorizedException() {super();}

    public ArtistryUnauthorizedException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.UNAUTHORIZED; }
}
