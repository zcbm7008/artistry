
package com.artistry.artistry.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequest {

    @NonNull
    private String nickName;
    private String iconUrl;
    private List<LinkRequest> links;

    public MemberUpdateRequest(String nickName, String iconUrl){
        this(nickName,iconUrl,new ArrayList<>());
    }

}
