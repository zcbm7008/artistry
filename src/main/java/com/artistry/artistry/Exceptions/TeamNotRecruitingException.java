package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class TeamNotRecruitingException extends ArtistryException{

    public TeamNotRecruitingException() {super();}

    public TeamNotRecruitingException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.BAD_REQUEST; }
}
