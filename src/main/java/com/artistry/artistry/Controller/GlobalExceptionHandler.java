package com.artistry.artistry.Controller;

import com.artistry.artistry.Exceptions.ArtistryException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArtistryException.class)
    public ResponseEntity<ExceptionDto> handleArtistryException(ArtistryException ex){
        return ResponseEntity.status(ex.status())
                .body(new ExceptionDto(ex.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ExceptionDto{

        private String message;
    }
}
