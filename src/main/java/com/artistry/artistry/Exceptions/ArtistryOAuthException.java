package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryOAuthException extends ArtistryException{
    public ArtistryOAuthException() {super();}

    public ArtistryOAuthException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.UNAUTHORIZED; }
}
