package com.zzarbttoo.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter //setter은 안 사용하는 것이 낫다
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) //연관관계 필드는 toString 안하는 것이 좋다(무한루프에 걸릴 수 있다)
public class Member {

    @Id @GeneratedValue //jpa가 알아서 순차적인 값을 넣어준다
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    //JPA 연관관계는 모두 Lazy로 setting해야 성능 최적화가 용이하다
    //지연로딩은 그 객체를 조회할 때는 그 객체 관련된 것만 조회할 수 있도록 하고
    //연관 관계까지 조회 하려고 할 때만 늦게 연관관계를 불러올 수 있도록 한다
    @ManyToOne(fetch = FetchType.LAZY) //다대 일 관계
    @JoinColumn(name = "team_id")
    private Team team;


    //entity는 기본적으로 default 생성자가 있어야 한다(protected까지는 열어놔야 함)
    //lombok으로 처리해놨다
    //protected Member(){
    //}

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if (team != null){ //일단은 team 이 null이여도 무시함
            changeTeam(team);
        }
    }

    //team을 변경
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);  //team에 있는 member의 setting도 변경
    }

}
