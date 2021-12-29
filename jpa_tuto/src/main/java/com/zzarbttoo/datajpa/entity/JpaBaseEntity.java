package com.zzarbttoo.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass //진짜 상속 관계는 아니고 속성을 공유할 수 있도록 한다
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) //create date는 업데이트 불가
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //저장 전에 이벤트 발생
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }

}
