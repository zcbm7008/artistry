package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class MemberDuplicatedException extends ArtistryException{
    public MemberDuplicatedException() {super();}

    public MemberDuplicatedException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }

}
