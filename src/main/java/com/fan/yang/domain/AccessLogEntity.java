package com.fan.yang.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zhang_fan
 * @since 2023/1/11 下午 06:44
 */
@Data
@Entity(name = "access_log")
public class AccessLogEntity {
    @Id
    @Column
    private String userId;

    @Column
    private Long visits;
}
