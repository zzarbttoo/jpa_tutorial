package com.zzarbttoo.datajpa.dto;

import com.zzarbttoo.datajpa.entity.Member;
import lombok.Data;

//entity에는 @Data 쓰면 안된다
@Data
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;

    public MemberDTO(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    //DTO는 entity를 받아도 된다
    public MemberDTO(Member member){
        this.id = member.getId();
        this.username = member.getUsername();

    }
}
