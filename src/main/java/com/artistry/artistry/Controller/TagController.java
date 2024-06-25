package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Request.TagUpdateRequest;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/name")
    public ResponseEntity<List<TagNameResponse>> findTagNames(@RequestParam(defaultValue = "") final String name){
        return ResponseEntity.ok(tagService.findTagNames(name));
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody final TagCreateRequest request){
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable final Long tagId,
                                                 @Valid @RequestBody final TagUpdateRequest request){
        TagResponse response = tagService.updateTag(tagId,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable final Long tagId){
        tagService.delete(tagId);
        return ResponseEntity.noContent().build();
    }

}
