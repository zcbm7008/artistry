package com.artistry.artistry.Domain.member;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@Embeddable
public class ProfileImage {
    public static final int SIZE = 110;

    public static final String INIT_URL = "https://artistry-bucket.s3.ap-northeast-2.amazonaws.com/INIT/thumbnail2.png";

    private String url;

    public ProfileImage(){
        this.url = INIT_URL;
    }

}
