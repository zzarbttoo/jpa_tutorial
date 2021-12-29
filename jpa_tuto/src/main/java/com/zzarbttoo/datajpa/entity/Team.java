package com.zzarbttoo.datajpa.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "level"})
//public class Team extends JpaBaseEntity{
public class Team extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    //연관관계가 있을 때 한쪽에 mappedBy를 걸어야 하는데 foreign key가 없는 곳에 거는 것이 좋다
    @OneToMany(mappedBy = "team") //1대 다 관계
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
