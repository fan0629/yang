package com.fan.yang.repository;

import com.fan.yang.domain.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhang_fan
 * @since 2023/1/11 下午 06:52
 */
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, String> {
    Long findVisitsByUserId(String value);
}
