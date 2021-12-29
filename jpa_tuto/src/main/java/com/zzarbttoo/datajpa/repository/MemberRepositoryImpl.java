package com.zzarbttoo.datajpa.repository;


import com.zzarbttoo.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor //final일 경우 injection 해준다
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    //class 이름을 repository이름 + Impl 형식으로 맞추어야 spring이 알아서 처리를 해준다


    //@PersistenceContext
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {

        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
