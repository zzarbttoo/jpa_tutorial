package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext //spring boot 컨테이너가 entityManger을 가져다준다
    private EntityManager em; //이걸 이용해서 jpa 가 동작

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){

        //jpql 사용
        //table이 아니라 객체를 대상으로 하는 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //null일 수도 있고 아닐 수도 있다(java8의 기본 기능!)
    }

    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
        .getSingleResult(); //단건
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username){
        //이름을 부여하고 불러올 수 있다
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();


    }

    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) //어디서부터 가져올 것인가
                .setMaxResults(limit) //몇개 넘길 것인가
                .getResultList();
    }

    public long totalCount(int age){
        //sorting 필요없어서 빼줬다
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
