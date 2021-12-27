package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//구현체 없이 기능이 동작하는 중
//Entity, Pk에 mapping 된 type
//implements 해서 이용 불가능(다 구현헤야함) -> custom 기능 이용
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username); //구현하지 않아도 동작함
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    //길다.....

    //List<Member> findHelloBy();
    List<Member> findTop3HelloBy(); //limit 3

}
