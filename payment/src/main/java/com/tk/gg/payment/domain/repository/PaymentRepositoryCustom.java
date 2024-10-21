package com.tk.gg.payment.domain.repository;

import com.tk.gg.payment.application.dto.PaymentResponseDto;
import com.tk.gg.payment.application.dto.PaymentSearchCondition;
import com.tk.gg.payment.domain.model.Payment;
import com.tk.gg.payment.domain.type.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryCustom {
    List<Payment> findSettleablePayments(Long userId, LocalDateTime startDate, LocalDateTime endDate, PaymentStatus paymentStatus);

    /**
     * 결제 검색을 위한 사용자 정의 쿼리 메서드
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 검색 결과를 담은 Page 객체
     */
    Page<Payment> searchPaymentsByCondition(PaymentSearchCondition condition, Pageable pageable);


}
