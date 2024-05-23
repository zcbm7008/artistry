package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/api/tags")
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(final TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponse> getTag(@PathVariable final Long tagId) {
        return ResponseEntity.ok(tagService.findById(tagId));
    }
}
