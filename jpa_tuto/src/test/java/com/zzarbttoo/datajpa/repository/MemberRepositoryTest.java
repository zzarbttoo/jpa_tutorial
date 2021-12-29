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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    //벌크성 업데이트를 할 때 DB에 직접 반영을 하기 때문에 entity에는 반영이 안된다
    //그래서 업데이트 후에는 entity를 전부 날리고 다시 해야한다
    //그래서 영속성 컨텍스트 매니저를 이용한다
    @PersistenceContext
    EntityManager entityManager;

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

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);

        //데이터를 날린다
        //벌크 연산 이후에는 꼭 영속성 컨텍스트를 날려야한다
        //entityManager.flush();
        //entityManager.clear();

        //혹은 modifying 옵션을 정한다

        List<Member> members = memberRepository.findListByUsername("member5");
        Member member5 = members.get(0);

        System.out.println("member 5 ::: " + member5);

        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberlazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teanA");
        Team teamB = new Team("teanB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        //when N + 1 문제
        //-> patch join으로 해결
        //select Member 1
        //List<T> members = memberRepository.findAll(); //member만 db에서 긁어옴
        //List<Member> members = memberRepository.findAll(); //override 한 상태, 전체를 긁어오도록 EntityGraph 이용
        //List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1"); //메소드명 + EntityGraph

        for(Member member : members){
            System.out.println("member ::: " + member.getUsername());
            //null로 남겨둘 수 없기 때문에 proxy 이용
            System.out.println("member teamClass ::: " + member.getTeam().getClass());

            //실제로 필요하면 db에서 들고오게 된다
            System.out.println("team:::" + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        //given
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //when
        //Optional<Member> byId = memberRepository.findById(member1.getId());
        //Member findMember = byId.get();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");

        findMember.setUsername("member2"); //변경 감지를 아예 안하기 때문에 update 진행하지 않음

        entityManager.flush();


    }

    @Test
    public void lock(){
        Member member1 = new Member("member1", 10);
        //given
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

        //lock
        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }


}