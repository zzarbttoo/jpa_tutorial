package com.zzarbttoo.datajpa.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {

    //@GeneratedValue //이걸 사용할 경우에는 persist를 이용
    //private Long id;

    @Id
    private String id; //직접 id 값을 넣을 경우에 그냥 하면 merge가 호출됨(merge는 select가 한번 호출돼서 비효율적)
    //그래서 Persistable을 상속받아서 직접 새로운 객체인지 여부를 알려줘야한다
    //직접 알려줄 때 createdDate null 여부에 따라 기존에 있던 객체인지 새로운 객체인지를 판별해서 알려준다

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId(){
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null; //새로운 객체인지 여부
    }




}
