package com.tk.gg.payment.application.dto;

import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.type.PayType;
import com.tk.gg.payment.domain.type.PaymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    // 결제 호출 요청용 Dto

    @NotNull(message = "결제 타입은 필수입니다.")
    private PayType payType; // 결제 타입 : 카드/현금/포인트

    @NotNull(message = "결제 금액은 필수입니다.")
    @Positive(message = "결제 금액은 양수여야 합니다.")
    private Long amount; // 가격 정보

    @NotBlank(message = "상품명은 필수입니다.")
    private String orderName;

    @NotNull(message = "예약 ID는 필수입니다.")
    private UUID reservationId;

    @Email
    @NotNull(message = "서비스 제공자의 이메일은 필수입니다.")
    private String customerEmail;

    @Builder.Default
    private PaymentStatus status = PaymentStatus.REQUESTED;


    private UUID couponId;

    private String yourSuccessUrl; // 성공 시 리다이렉트 될 URL
    private String yourFailUrl; // 실패 시 리다이렉트 될 URL

}
