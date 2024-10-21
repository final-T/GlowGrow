package com.tk.gg.payment.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pending_payment_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingPaymentRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID reservationId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long serviceProviderId;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static PendingPaymentRequest create(UUID reservationId, Long customerId, Long serviceProviderId, Integer price) {
        PendingPaymentRequest request = new PendingPaymentRequest();
        request.reservationId = reservationId;
        request.customerId = customerId;
        request.serviceProviderId = serviceProviderId;
        request.price = price;
        request.createdAt = LocalDateTime.now();
        return request;
    }

}
