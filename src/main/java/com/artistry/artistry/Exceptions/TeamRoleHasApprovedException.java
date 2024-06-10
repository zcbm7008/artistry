package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class TeamRoleHasApprovedException extends  ArtistryException{

    public TeamRoleHasApprovedException() {super();}

    public TeamRoleHasApprovedException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.BAD_REQUEST; }
}
