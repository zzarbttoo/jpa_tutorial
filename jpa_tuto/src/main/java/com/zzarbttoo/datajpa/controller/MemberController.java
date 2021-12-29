package com.zzarbttoo.datajpa.controller;

import com.zzarbttoo.datajpa.dto.MemberDTO;
import com.zzarbttoo.datajpa.entity.Member;
import com.zzarbttoo.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){

        Member member = memberRepository.findById(id).get();
        return member.getUsername();

    }

    //pk를 이용해서 호출 가능
    //조회용으로만 호출을 해야한다
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    //PageRequest 생성
    //http://localhost:8080/members?page=0&size=3&sort=id,desc&sort=username,desc
    //global 설정은 application.yml 파일에 할 수 있음
    //페이징 정보가 둘 이상이면 접두사로 구분
    @GetMapping("/members")
    public Page<MemberDTO> list(@PageableDefault(size = 5, sort = "username")Pageable pageable){

        //기본 함수에 pageable 파라미터를 넘기면 된다
        //Page<Member> page = memberRepository.findAll(pageable);

        //entity -> DTO
        //Page<MemberDTO> map = page.map(member -> new MemberDTO(member.getId(), member.getUsername(), null));
        //Page<MemberDTO> map = page.map(member -> new MemberDTO(member));

        //return map; //실무에서는 그냥 inline으로 보낸다

        return memberRepository.findAll(pageable)
                .map(MemberDTO::new);
    }
    //1부터 시작하게 하려면 Pageable 직접 구현해야한다




    //데이터 없어서 임시로 넣음
    //@PostConstruct
    public void init(){
        //memberRepository.save(new Member("userA"));
        for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
