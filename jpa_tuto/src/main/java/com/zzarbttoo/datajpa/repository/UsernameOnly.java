package com.zzarbttoo.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //@Value("#{target.username + ' ' + target.age}")  //open projection -> entity를 다 들고와서 계산해준다
    String getUsername();


}
