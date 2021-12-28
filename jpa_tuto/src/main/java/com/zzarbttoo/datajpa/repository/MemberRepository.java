package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.dto.MemberDTO;
import com.zzarbttoo.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//구현체 없이 기능이 동작하는 중
//Entity, Pk에 mapping 된 type
//implements 해서 이용 불가능(다 구현헤야함) -> custom 기능 이용
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username); //구현하지 않아도 동작함
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    //길다.....

    //List<Member> findHelloBy();
    List<Member> findTop3HelloBy(); //limit 3

    @Query(name = "Member.findByUsername")
    List<Member> findByUsernameByNamedQuery(@Param("username") String username);

    //Entity.namedQuery -> 메서드 이름으로 찾기 순서대로 된다
    //그런데 네임드 쿼리 잘 안쓴다
    //네임드 쿼리의 장점은 application 로딩 시점에 파싱을 해보기 때문에 오류를 미리 발견할 수 있다


    //repository에 쿼리를 바로 작성할 수 있다(이건 많이 씀)
    //간략한 이름
    //네임드 쿼리와 마찬가지로 application 로딩 시점에서 오류를 잡아준다
    //이름 없는 namedQuery라고 생각하면 된다
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO로 조회할 때 new operation을 사용해야한다
    //패키지 이름을 다 써줘야 한다(생성자로 조회하는 것처럼)
    @Query("select new com.zzarbttoo.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();


    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 opiontal

    //페이징 처리
    //Page<Member> findByAge(int age, Pageable pageable);
    //Slice<Member> findByAge(int age, Pageable pageable);
    //List<Member> findByAge(int age, Pageable pageable);

    //count 쿼리는 join같은 것을 할 필요가 없기 때문에(갯수는 어차피 같음) 분리가 가능
    //count 쿼리 따로 안쓰면 개느림
    //sorting 조건 같은거도 더 복잡해지면 따로 빼서 짜면 된다!
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);




}
