package com.artistry.artistry.Exceptions;

import org.springframework.http.HttpStatus;

public abstract class ArtistryException extends RuntimeException{

    public ArtistryException(){
    }

    public ArtistryException(final String message){
        super(message);
    }

    public abstract HttpStatus status();
}
