package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public class PortfolioNotFoundException extends ArtistryException{
    public PortfolioNotFoundException() {super();}

    public PortfolioNotFoundException(final String message) { super(message);}

    @Override
    public HttpStatus status() {return HttpStatus.NOT_FOUND; }
}
