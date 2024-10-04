package com.tk.gg.post;

import com.tk.gg.post.application.service.PostService;
import com.tk.gg.post.domain.model.Post;
import com.tk.gg.post.domain.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
//TODO : 좋아요 수 떨어지는 것도 동시성 여부 체크 필요?

@SpringBootTest
@Transactional
@Slf4j
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private UUID postId;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 게시물 생성
//        Post post = Post.createPostBuilder()
//                .postRequestDto(new PostRequestDto("Test Title", "Test Content"))
//                .build();
//        postRepository.save(post);
        postId = UUID.fromString("c4e50cbb-2b04-4536-b0a8-08d1832f7b57");
    }

    @Test
    @Transactional
    void multiThreadsLikesTest() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // 여러 사용자가 동시에 좋아요를 누름
        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;  // 각 사용자마다 고유한 ID 할당
            executorService.execute(() -> {
                try {
                    postService.updatePostLikeCount(postId, true);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // 게시물 좋아요 수 검증
        Post post = postService.getPostById(postId);
        assertThat(post.getLikes()).isEqualTo(numberOfThreads);
        // 결과 확인을 위한 print문
        System.out.println("테스트 성공: 게시물 좋아요 수가 " + post.getLikes() + "로 예상한 수치와 일치합니다.");
    }
}
