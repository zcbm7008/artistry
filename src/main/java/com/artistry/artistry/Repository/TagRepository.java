package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
}