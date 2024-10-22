package com.tk.gg.post;

import com.tk.gg.post.application.dto.PostRequestDto;
import com.tk.gg.post.application.service.PostService;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
import com.tk.gg.security.user.AuthUserInfo;
import com.tk.gg.security.user.AuthUserInfoImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PostApplication.class)
@Slf4j
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private UUID postId;

    @BeforeEach
    void setUp() {
        AuthUserInfo testUser = AuthUserInfoImpl.builder()
                .id(1L)
                .build();

        Post post = Post.createPostBuilder()
                .postRequestDto(new PostRequestDto("LIKE Race condition Test Title", "Test Content"))
                .authUserInfo(testUser)
                .build();

        Post savedPost = postRepository.save(post);
        postRepository.flush();
        this.postId = savedPost.getPostId();
    }

    @Test
    void loadTest() throws InterruptedException {
        int numberOfThreads = 100;  // 쓰레드 수
        int loopCount = 10;         // 루프 카운트
        int rampUpTimeSeconds = 1;  // Ramp-up 시간(초)

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads * loopCount);

        // 성능 측정을 위한 지표들
        AtomicInteger successCount = new AtomicInteger(0);
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        long startTime = System.currentTimeMillis();

        // Ramp-up을 위한 지연 계산
        long delayPerThread = (rampUpTimeSeconds * 1000L) / numberOfThreads;

        // 각 쓰레드별 작업 실행
        for (int threadNum = 0; threadNum < numberOfThreads; threadNum++) {
            final int threadIndex = threadNum;

            // Ramp-up을 위한 지연 추가
            Thread.sleep(delayPerThread);

            executorService.execute(() -> {
                for (int i = 0; i < loopCount; i++) {
                    long requestStartTime = System.nanoTime();
                    try {
                        postService.updatePostLikeCount(postId, true);
                        successCount.incrementAndGet();
                        // 응답 시간 기록 (밀리초 단위)
                        responseTimes.add((System.nanoTime() - requestStartTime) / 1_000_000);
                    } catch (Exception e) {
                        log.error("Error in thread {}, iteration {}: {}", threadIndex, i, e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        // 모든 작업 완료 대기
        latch.await();
        long endTime = System.currentTimeMillis();
        executorService.shutdown();

        long totalTime = endTime - startTime;
        double avgResponseTime = responseTimes.stream()
                .mapToLong(Long::valueOf)
                .average()
                .orElse(0.0);
        double throughput = (successCount.get() * 1000.0) / totalTime;
        double successRate = (successCount.get() * 100.0) / (numberOfThreads * loopCount);

        // 테스트 결과 출력
        log.info("부하 테스트 결과:");
        log.info("총 요청 수: {}", numberOfThreads * loopCount);
        log.info("성공 요청 수: {}", successCount.get());
        log.info("평균 응답 시간: {}ms", String.format("%.2f", avgResponseTime));
        log.info("처리량: {} requests/second", String.format("%.2f", throughput));
        log.info("성공률: {}%", String.format("%.2f", successRate));

        // 최종 좋아요 수 검증
        Post post = postService.getPostById(postId);
        assertThat(post.getLikes()).isEqualTo(successCount.get());
    }
}