package com.tk.gg.reservation.domain.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManagerFactory entityManagerFactory) {
        return new JPAQueryFactory(entityManagerFactory.createEntityManager());
    }

    //TODO : request 헤더 기반으로 AuditAware 넣기??
}
