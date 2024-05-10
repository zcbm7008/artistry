package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequestDto {
    private Long teamId;
    private Long hostId;
    private List<Member> members;
    private List<Role> roles;
    private List<Tag> tags;
}