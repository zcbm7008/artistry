package com.artistry.artistry.Domain.portfolio;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Embeddable
public class Content {

    private String url;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ContentsType type;

    public Content(String url, String comment){
        this(url,comment,ContentsType.UNKNOWN);
    }

    public Content(String url, String comment, ContentsType type){
        this.url = url;
        this.comment = comment;
        this.type = type;
    }

}
