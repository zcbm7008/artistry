package com.artistry.artistry.Domain;

import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import java.util.HashMap;
import java.util.Map;

public class RoleMember {

    private final Map<Role,Member> roleMemberMap;

    public RoleMember() {
        this.roleMemberMap = new HashMap<>();
    }

    public void add(Role role, Member member){
        isValid(role,member);
        roleMemberMap.put(role,member);
    }

    public boolean contains(Role role, Member member){
        if(roleMemberMap != null){
            return roleMemberMap.containsKey(role) && roleMemberMap.containsValue(member);
        }
        return true;

    }

    public boolean isValid(Role role,Member member){
        if(contains(role,member)){
            throw new ArtistryDuplicatedException("이미 해당 포지션에 지원한 포트폴리오가 있습니다.");
        }
        return true;
    }

    public Map<Role, Member> getRoleMemberMap() {
        return roleMemberMap;
    }
}
