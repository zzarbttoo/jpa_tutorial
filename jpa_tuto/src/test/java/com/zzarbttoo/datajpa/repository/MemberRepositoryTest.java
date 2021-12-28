package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.dto.MemberDTO;
import com.zzarbttoo.datajpa.entity.Member;
import com.zzarbttoo.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get(); //원래는 값이 있는지 업는지 여부를 다 확인해줘야함

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);


    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member!!!!"); //변경 감지를 통한 변경
//
//        //리스트 조회 검증
//        List<Member> all = memberJpaRepository.findAll();
//        assertThat(all.size()).isEqualTo(2);
//
//        //카운트 검증
//        long count = memberJpaRepository.count();
//        assertThat(count).isEqualTo(2);
//
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long deletedCount = memberJpaRepository.count();
//        assertThat(deletedCount).isEqualTo(0);


    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);


    }

    //find ~ By 형식이 아니라면 그냥 전체조회를 해온다
    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();

    }

    @Test
    public void testNamedQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameByNamedQuery("AAA");
        Member findMember = result.get(0);

        assertThat(findMember).isEqualTo(m1);

    }

    @Test
    public void testQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //List<Member> result = memberRepository.findUser("AAA", 10);
        //Member findMember = result.get(0);

        //assertThat(findMember).isEqualTo(m1);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findMemberDTO() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDTO> memberDTOList = memberRepository.findMemberDTO();

        for (MemberDTO dto : memberDTOList) {
            System.out.println(dto.toString());
        }


    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findListByUsername("AAA");
        System.out.println(memberList);

        List<Member> emptyList = memberRepository.findListByUsername("XXX");
        System.out.println(emptyList.size()); //null을 반환하는 것이 아니라 empty collection을 반환해줌

        Member findMember = memberRepository.findMemberByUsername("AAA");
        System.out.println(findMember); //단건은 결과가 없다면 null을 반환한다

        Optional optional = memberRepository.findOptionalByUsername("AAA");
        System.out.println(optional.get());


    }


    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        int age = 10;

        //page를 0부터 시작
        //3페이지씩
        //sorting 조건들
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        //page를 반환해야 하면 total count를 자동으로 한다
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        //List<Member> page = memberRepository.findByAge(age, pageRequest);

        //Entity를 DTO로 편하게 변환시킬 수 있다
        Page<MemberDTO> toMap = page.map(member -> new MemberDTO(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); //total count와 같은 역할

        for(Member member : content){
            System.out.println("member ::: " + member);
        }

        System.out.println("count ::: " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호도 가져올 수 있다
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 수
        assertThat(page.isFirst()).isTrue(); //첫페이지 여부
        assertThat(page.hasNext()).isTrue(); //다음 페이지 여부

    }
}