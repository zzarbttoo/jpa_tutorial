package com.zzarbttoo.datajpa.repository;


import com.zzarbttoo.datajpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ItemRepository extends JpaRepository<Item, Long> {


}
