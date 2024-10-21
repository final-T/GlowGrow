package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.type.CouponType;
import com.tk.gg.payment.domain.type.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PaymentRequestDto {
    // 결제 호출 요청용 Dto
    private UUID paymentId;

    @NotNull(message = "결제 금액은 필수입니다.")
    @Positive(message = "결제 금액은 양수여야 합니다.")
    private Long amount; // 가격 정보

    @NotBlank(message = "상품명은 필수입니다.")
    private String orderName;

    @NotNull(message = "예약 ID는 필수입니다.")
    private UUID reservationId;

    private Long userId;

    private Long customerId;

    @Builder.Default
    private PaymentStatus status = PaymentStatus.REQUESTED;

    @Setter
    @Getter
    private String token;

    private UUID couponId;
    private CouponType couponType;
    private Long originalAmount;
    private Long discountAmount;
    private String couponcode; // 쿠폰 타입 저장을 위한 couponCode


    private String yourSuccessUrl; // 성공 시 리다이렉트 될 URL
    private String yourFailUrl; // 실패 시 리다이렉트 될 URL
}
