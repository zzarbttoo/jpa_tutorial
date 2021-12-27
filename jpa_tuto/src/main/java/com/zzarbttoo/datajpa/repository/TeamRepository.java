package com.zzarbttoo.datajpa.repository;

import com.zzarbttoo.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

//repository annotation 생략 가능
public interface TeamRepository extends JpaRepository<Team, Long> {
}
