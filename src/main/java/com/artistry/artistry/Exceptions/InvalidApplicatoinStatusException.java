package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidApplicatoinStatusException extends ArtistryException{
    public InvalidApplicatoinStatusException() {super();}

    public InvalidApplicatoinStatusException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
