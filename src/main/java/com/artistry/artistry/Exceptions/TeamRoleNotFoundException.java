package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class TeamRoleNotFoundException extends  ArtistryException{

    public TeamRoleNotFoundException() {super();}

    public TeamRoleNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
