package com.tk.gg.common.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 설정
 * createdAt, updatedAt 자동으로 관리하기 위한 설정
 * deleteAt, deletedBy, createdBy, updatedBy는 각 서비스에서 직접 관리
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
