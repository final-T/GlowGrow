package com.tk.gg.promotion;

import com.tk.gg.promotion.application.dto.CouponIssueRequestDto;
import com.tk.gg.promotion.application.serivce.CouponApplicationService;
import com.tk.gg.promotion.domain.Promotion;
import com.tk.gg.promotion.domain.enums.DiscountType;
import com.tk.gg.promotion.domain.enums.PromotionStatus;
import com.tk.gg.promotion.infrastructure.repository.CouponUserRepository;
import com.tk.gg.promotion.infrastructure.repository.PromotionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class CouponIssuanceTest {
    @Autowired
    private CouponApplicationService couponApplicationService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CouponUserRepository couponUserRepository;

    private UUID promotionId;
    private UUID couponId;

    @BeforeEach
    public void setUp() {
        // 프로모션 생성
        Promotion promotion = Promotion.builder()
                .title("프로모션 제목")
                .description("프로모션 설명")
                .postUserId(1L)
                .startDate(LocalDate.parse("2021-01-01"))
                .endDate(LocalDate.parse("2021-12-31"))
                .status(PromotionStatus.ACTIVE)
                .build();


        // 쿠폰 생성
        promotion.createCoupon(
                "쿠폰 설명",
                DiscountType.AMOUNT,
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(100000),
                LocalDate.parse("2021-01-01"),
                LocalDate.parse("2021-12-31"),
                100
        );

        // 프로모션 저장
        promotionRepository.save(promotion);

        promotionId = promotion.getPromotionId();
        couponId = promotion.getCoupons().get(0).getCouponId();

    }


    @Test
    @DisplayName("쿠폰 동시성 테스트")
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

        int issuedCouponCount = couponUserRepository.countByCouponId(couponId);
        System.out.println("발급된 쿠폰 수량: " + issuedCouponCount);

        // 발급된 쿠폰 수량이 100개인지 확인
        Assertions.assertThat(issuedCouponCount).isEqualTo(100);
    }
}

