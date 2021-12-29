package com.zzarbttoo.datajpa.entity;

import com.zzarbttoo.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush(); //jpa 영속성 컨텍스트에 저장을 미리 해놓는데 강제로 flush를 하면 DB에 쿼리를 날린다
        em.clear(); //쿼리를 날린 후 영속성 컨텍스트에 있는 캐시를 다 날린다

        List<Member> members = em.createQuery("select m from Member m ", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member ::: " + member);
            System.out.println("-> member.team ::: " + member.getTeam());
        }

        }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //@PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.createDate :::" + findMember.getCreatedDate());
        //System.out.println("findMember.updateDate :::" + findMember.getUpdatedDate());
        System.out.println("findMember.updateDate :::" + findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy :::"  + findMember.getCreatedBy());
        System.out.println("findMember.updatedBy :::" + findMember.getLastModifiedBy());


    }


    }
