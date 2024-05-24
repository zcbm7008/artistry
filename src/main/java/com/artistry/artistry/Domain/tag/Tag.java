package com.artistry.artistry.Domain.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private TagName name;

    public Tag(final String name) {
        this(null,name);
    }
    public Tag(final Long id, final String name) {
        this.id = id;
        this.name = new TagName(name);
    }

    public void update(String name){
        this.name = new TagName(name);
    }

    public String getName(){
        return name.getValue();
    }

}
