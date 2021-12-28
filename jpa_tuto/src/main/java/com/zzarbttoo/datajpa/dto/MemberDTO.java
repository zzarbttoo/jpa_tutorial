package com.zzarbttoo.datajpa.dto;

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
}
