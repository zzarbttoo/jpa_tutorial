package com.zzarbttoo.datajpa.repository;

public class UsernameOnlyDTO {

    private final String username;


    //생성자의 parameter 이름으로 matching 해서 projection이 가능하다
    public UsernameOnlyDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
