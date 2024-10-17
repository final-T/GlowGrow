package com.tk.gg.promotion;

import com.tk.gg.promotion.application.dto.CouponCreateRequestDto;
import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import com.tk.gg.promotion.domain.Coupon;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.DiscountType;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import com.tk.gg.promotion.domain.service.CouponDomainService;
import com.tk.gg.promotion.domain.service.PromotionDomainService;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
@Transactional
class CouponIssuanceTest {
    @Autowired
    private CouponApplicationService couponApplicationService;

    @Autowired
    private CouponUserRepository couponUserRepository;

    @Autowired
    private PromotionDomainService promotionDomainService;

    @Autowired
    private CouponDomainService couponDomainService;

    private UUID promotionId;
    private UUID couponId;

    @BeforeEach
    public void setUp() {
        // 프로모션 생성
        Promotion promotion = promotionDomainService.createPromotion(Promotion.builder()
                .title("프로모션 제목")
                .description("프로모션 설명")
                .postUserId(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .status(PromotionStatus.ACTIVE)
                .build(), null);

        promotionId = promotion.getPromotionId();
        System.out.println("프로모션 ID: " + promotionId);

        // 쿠폰 생성
        Coupon coupon = couponDomainService.createCoupon(CouponCreateRequestDto.builder()
                .promotionId(promotion.getPromotionId())
                .description("쿠폰 설명")
                .discountType(DiscountType.AMOUNT)
                .discountValue(BigDecimal.valueOf(10000))
                .maxDiscount(BigDecimal.valueOf(100000))
                .validFrom(LocalDate.now())
                .validUntil(LocalDate.now().plusDays(10))
                .totalQuantity(100)
                .build());

        couponId = coupon.getCouponId();
        System.out.println("쿠폰 ID: " + couponId);
    }


    @Test
    @DisplayName("쿠폰 동시성 테스트")
    // 트랜잭션을 사용하지 않도록 설정
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void testConcurrentCouponIssuance() throws Exception {
        // 동시에 쿠폰 발급 요청
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 총 수행 시간 측정
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            final long userId = i; // 각 쓰레드마다 고유한 사용자 ID 사용
            executorService.execute(() -> {
                try {
                    CouponIssueRequestDto couponIssueRequestDto = CouponIssueRequestDto.builder()
                            .promotionId(promotionId)
                            .couponId(couponId)
                            .userId(userId)
                            .build();

                    // 쿠폰 발급
                    couponApplicationService.issueCoupon(couponIssueRequestDto);

                } catch (Exception e) {
                    // 예상되는 예외 처리 (쿠폰 소진 예외 등)
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        long end = System.currentTimeMillis();

        // 총 수행 시간 출력
        System.out.println("총 수행 시간: " + (end - start) + "ms");


        // 비동기로 처리 되기 때문에 발급된 쿠폰 수량을 확인하기 위해 3초 대기
        Thread.sleep(3000);

        int issuedCouponCount = couponUserRepository.countByCouponId(couponId);
        System.out.println("발급된 쿠폰 수량: " + issuedCouponCount);

        // 발급된 쿠폰 수량이 100개인지 확인
        Assertions.assertThat(issuedCouponCount).isEqualTo(100);
    }
}

