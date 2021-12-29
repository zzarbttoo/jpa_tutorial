package com.zzarbttoo.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing //auditing을 위해 꼭 추가
@SpringBootApplication
public class JpaTutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaTutoApplication.class, args);
    }

    //최초 생성자, 수정한 사람 저장
    @Bean
    public AuditorAware<String> auditorProvider(){

        //Spring Security 정보를 가져와서 id를 넣어주거나 해야한다
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
