package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class ArtistryTooManyRequests extends ArtistryException{
    public ArtistryTooManyRequests() {super();}

    public ArtistryTooManyRequests(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.TOO_MANY_REQUESTS; }
}
