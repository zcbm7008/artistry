package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamSearchRequest {
    private String name;
    private List<Long> roleIds;
    private List<Long> tagIds;
    private TeamStatus teamStatus;

}
