package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.entity.Member;
import com.zzarbttoo.datajpa.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamName){

        Specification<Member> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                if (StringUtils.isEmpty(teamName)) {
                    return null;
                }

                Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 join
                return criteriaBuilder.equal(t.get("name"), teamName);
            }

            ;
        };
        return specification;

    }

    public static Specification<Member> username(final String username){
        return (Specification<Member>) (root, query, builder) -> builder.equal(root.get("username"), username);

    }

}
