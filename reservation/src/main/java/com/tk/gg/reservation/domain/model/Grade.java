package com.tk.gg.reservation.domain.model;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.reservation.application.dto.GradeDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

//TODO : 엔티티 필드 이름 수정 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Table(name = "p_grades")
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "review_id")
    private UUID reviewId;
    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userType; // PROVIDER 또는 CUSTOMER

    // 제공자 (디자이너) 평가 항목들
    @Column(name = "service_quality")
    private Integer providerServiceQuality;  // 서비스 품질

    @Column(name = "professionalism")
    private Integer providerProfessionalism;  // 전문성

    @Column(name = "communication")
    private Integer providerCommunication;  // 의사소통

    @Column(name = "punctuality")
    private Integer providerPunctuality;  // 시간 준수

    @Column(name = "price_satisfaction")
    private Integer providerPriceSatisfaction;  // 가격 적정성

    // 고객 평가 항목들
    @Column(name = "customer_communication")
    private Integer customerCommunication;  // 고객 의사소통

    @Column(name = "customer_punctuality")
    private Integer customerPunctuality;  // 고객 시간 준수

    @Column(name = "customer_manners")
    private Integer customerManners;  // 고객 매너

    @Column(name = "payment_promptness")
    private Integer customerPaymentPromptness;  // 결제 신속성

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    public void update(GradeDto dto) {
        this.reviewId = dto.reviewId();
        this.reservationId = dto.reservationId();
        this.userId = dto.userId();
        this.userType = dto.userType();
        this.providerServiceQuality = dto.providerServiceQuality();
        this.providerProfessionalism = dto.providerProfessionalism();
        this.providerCommunication = dto.providerCommunication();
        this.providerPunctuality = dto.providerPunctuality();
        this.providerPriceSatisfaction = dto.providerPriceSatisfaction();
        this.customerCommunication = dto.customerCommunication();
        this.customerPunctuality = dto.customerPunctuality();
        this.customerManners = dto.customerManners();
        this.customerPaymentPromptness = dto.customerPaymentPromptness();
    }
}
