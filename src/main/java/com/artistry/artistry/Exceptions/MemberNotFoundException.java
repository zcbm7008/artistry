package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ArtistryException{
    public MemberNotFoundException() {super();}

    public MemberNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
