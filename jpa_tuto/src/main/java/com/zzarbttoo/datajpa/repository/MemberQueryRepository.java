package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


//꼭 customRepository를 쓸 필요는 없다
//핵심 비지니스 로직과, 화면에 맞춘 로직은 repository를 구분해서 사용하는 것이 좋다
//커맨드/쿼리 분리, 핵심/부가 로직 분리 등을 다각적으로 고민해서 분리해서 개발하는 것이 좋다
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
