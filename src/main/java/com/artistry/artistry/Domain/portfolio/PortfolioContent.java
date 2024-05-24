package com.artistry.artistry.Domain.portfolio;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Embeddable
public class PortfolioContent {

    private String url;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ContentsType type;

    public PortfolioContent(String url,String comment, ContentsType type){
        this.url = url;
        this.comment = comment;
        this.type = type;
    }

}
