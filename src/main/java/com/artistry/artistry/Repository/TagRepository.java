package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    void delete(Tag tag);

    @Query(value = "select tag.id, tag.name \n "
        + "from tag \n"
        + "where tag.name like %:name%\n", nativeQuery = true)
    List<Tag> findAllByName(@Param("name") final String name);
}
