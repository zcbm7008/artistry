package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.RoleCreateRequest;
import com.artistry.artistry.Dto.Request.RoleUpdateRequest;
import com.artistry.artistry.Dto.Response.RoleResponse;
import com.artistry.artistry.Service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/roles")
@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(final RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable final Long tagId) {
        return ResponseEntity.ok(roleService.findById(tagId));
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody final RoleCreateRequest request){
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable final Long roleId,
                                           @Valid @RequestBody final RoleUpdateRequest request){
        RoleResponse response = roleService.updateRole(roleId,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable final Long roleId){
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
