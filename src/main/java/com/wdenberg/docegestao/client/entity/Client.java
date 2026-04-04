package com.wdenberg.docegestao.client.entity;


import com.wdenberg.docegestao.common.config.BaseEntity;
import com.wdenberg.docegestao.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @Column(name = "total_spent", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "last_order_at")
    private OffsetDateTime lastOrderAt;

    @Column(nullable = false)
    private Boolean active = true;
}
