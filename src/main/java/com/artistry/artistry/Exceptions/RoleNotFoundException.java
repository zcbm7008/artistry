package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ArtistryException{

    public RoleNotFoundException() {super();}

    public RoleNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
