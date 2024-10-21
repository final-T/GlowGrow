# GlowGrow

> 다양한 경험을 통해 실력을 높이고 싶은 인턴 디자이너들과 저렴한 가격으로 디자인을 받고 싶은 고객들을 연결해주는 예약 사이트

### 프로젝트 목표

- **대규모 트래픽**을 안정적으로 처리할 수 있는 C2C 예약 시스템을 구축하여, 인턴 디자이너와 고객 간의 원활한 매칭 및 예약이 가능하도록 합니다. **MSA 기반**으로 각 기능을 독립적으로 확장할 수
  있도록 설계하여 서비스 **유연성과 확장성을 극대화**합니다. 또한 실시간 알림 및 채팅을 통해 사용자 경험을 향상시키고, 프로모션, 리뷰, 결제 시스템을 통해 플랫폼의 신뢰성과 효율성을 강화합니다.
- **Kafka**를 활용한 비동기 통신으로 MSA 구조 내 서비스 간 통신 부담 최소화
- **Zipkin**, **Grafana**를 사용한 애플리케이션 모니터링으로 서비스 안정성 확보

<BR>

### 📚 STACKS

- **Tools** <br>
  <img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat&logo=Intellij IDEA&logoColor=white"/>
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white"/><img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white"/><img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=slack&logoColor=white"/>
  <br>
- **Monitoring & Logging** <br>
  <img src="https://img.shields.io/badge/Zipkin -FE5F50?style=flat&logoColor=white"/>
  <img src="https://img.shields.io/badge/Prometheus -E6522C?style=flat&logo=prometheus&logoColor=white"/>
  <img src="https://img.shields.io/badge/Grafana -F46800?style=flat&logo=grafana&logoColor=white"/>
  <br>
- **Frameworks & Library** <br>
  <img src="https://img.shields.io/badge/Java 17 -C70D2C?style=flat&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Springboot 3.3-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Springsecurity -6DB33F?style=flat&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jwt -black?style=flat-square&logo=JSON%20web%20tokens&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/QueryDSL-5395FD?style=flat&logo=QueryDSL&logoColor=white"/>
  <img src="https://img.shields.io/badge/postgresql -4169E1?style=flat&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/redis -FF4438?style=flat&logo=redis&logoColor=white"/><img src="https://img.shields.io/badge/H2 -0854C1?style=flat&logo=H2&logoColor=white"/>
  <img src="https://img.shields.io/badge/swagger -85EA2D?style=flat&logo=swagger&logoColor=white"/>
  <br>
- **Infra** <br>
  <img src="https://img.shields.io/badge/Apache%20Kafka -000?style=flat-square&logo=apachekafka&logoColor=white">
  <img src="https://img.shields.io/badge/Docker -2496ED?style=flat&logo=docker&logoColor=white"/>
  <br>
  <img src="https://img.shields.io/badge/Ec2 -FF9900?style=flat-square&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/S3 -569A31?style=flat-square&logo=amazons3&logoColor=white">
  <img src="https://img.shields.io/badge/RDS -527FFF?style=flat-square&logo=amazonrds&logoColor=white">

<BR>

## 👨‍👩‍👧‍👦 Our Team

|                     정호준                     |                    이원영                     |                  이정빈                  |                    박진우                     |
|:-------------------------------------------:|:------------------------------------------:|:-------------------------------------:|:------------------------------------------:|
|   [@hosu](https://github.com/junghojune)    | [@twonezero](https://github.com/TwOneZero) | [@ego2](https://github.com/jungbin97) | [@ParkJinWu](https://github.com/ParkJinWu) |
| 유저, 프로필<br>JWT 인증/인가<br>알림<br>서비스 배포, CI/CD |          예약 시스템<br>서비스 배포, CI/CD           |     프로모션, 쿠폰<br>채팅<br>모니터링 시스템 배포     |          게시판<br>결제<br>모니터링 시스템 배포          |

<br>

## API Spec

[🔗 API Spec (Swagger)](http://13.209.24.74:19091/webjars/swagger-ui/index.html)

## 문서 자료

[🔗 ERD](https://www.erdcloud.com/d/CxyebQgd99CDENisa) <br>

## 인프라 아키텍처

![image](https://github.com/user-attachments/assets/ac9500e3-7d39-41a7-98d9-fc70a1182812)


## 프로젝트 기능 및 서비스 구성

### 🔒 Auth

> 사용자 인증 기능을 제공합니다.

- <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>

| 메소드  | 엔드포인트               | 설명       |
|------|---------------------|----------|
| GET  | `/api/auth/info`    | 인증 정보 조회 |
| POST | `/api/auth/login`   | 로그인      |
| POST | `/api/auth/sign-up` | 회원가입     |

</ul>
  </div>
</details>

### 🧑‍ User & Profile

> 사용자 프로필 생성, 조회, 수정, 삭제 기능을 제공합니다.

- 사용자 프로필
  <details>
    <summary><b>EndPoint</b></summary>
    <div markdown="1">
    <ul>

| 메소드    | 엔드포인트                                                               | 설명            |
|--------|---------------------------------------------------------------------|---------------|
| GET    | `/api/profile`                                                      | 프로필 전체 조회     |
| POST   | `/api/profile`                                                      | 프로필 생성        |
| GET    | `/api/profile/my`                                                   | 내 프로필 조회      |
| GET    | `/api/profile/{{profileId}}`                                        | 특정 프로필 조회     |
| PUT    | `/api/profile/{{profileId}}`                                        | 프로필 수정        |
| POST   | `/api/profile/{{profileId}}/award`                                  | 프로필에 수상 경력 추가 |
| DELETE | `/api/profile/{{profileId}}/delete`                                 | 프로필 삭제        |
| DELETE | `/api/profile/{{profileId}}/delete/award/{{awardId}}`               | 수상 경력 삭제      |
| DELETE | `/api/profile/{{profileId}}/delete/experience/{{workExperienceId}}` | 경력 삭제         |
| DELETE | `/api/profile/{{profileId}}/delete/location/{{locationId}}`         | 위치 삭제         |
| DELETE | `/api/profile/{{profileId}}/delete/price/{{priceId}}`               | 가격 정보 삭제      |
| DELETE | `/api/profile/{{profileId}}/delete/style/{{styleId}}`               | 스타일 삭제        |
| POST   | `/api/profile/{{profileId}}/experience`                             | 경력 추가         |
| POST   | `/api/profile/{{profileId}}/location`                               | 위치 추가         |
| POST   | `/api/profile/{{profileId}}/price`                                  | 가격 추가         |
| POST   | `/api/profile/{{profileId}}/style`                                  | 스타일 추가        |

</ul>
</div>
</details>

- 유저 정보
  <details>
    <summary><b>EndPoint</b></summary>
    <div markdown="1">
    <ul>

| 메소드 | 엔드포인트                             | 설명             |
|-----|-----------------------------------|----------------|
| PUT | `/api/users`                      | 사용자 정보 수정      |
| GET | `/api/users/grade/my`             | 내 평점 조회        |
| GET | `/api/feign/user/exist/{{email}}` | 이메일로 사용자 존재 확인 |

</ul>
</div>
</details>

### 📝 Post (Comment, Like, Multimedia)

> 게시글, 댓글, 좋아요, 멀티미디어 기능을 제공합니다.

- 게시글
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드    | 엔드포인트                   | 설명        |
|--------|-------------------------|-----------|
| GET    | `/api/posts`            | 게시글 전체 조회 |
| POST   | `/api/posts`            | 게시글 생성    |
| GET    | `/api/posts/search`     | 게시글 검색    |
| GET    | `/api/posts/{{postId}}` | 특정 게시글 조회 |
| PATCH  | `/api/posts/{{postId}}` | 특정 게시글 수정 |
| DELETE | `/api/posts/{{postId}}` | 특정 게시글 삭제 |

</ul>
</div>
</details>

- 댓글 & 좋아요
  <details>
    <summary><b>EndPoint</b></summary>
    <div markdown="1">
    <ul>

| 메소드    | 엔드포인트                               | 설명             |
|--------|-------------------------------------|----------------|
| GET    | `/api/posts/comments/search`        | 댓글 검색          |
| GET    | `/api/posts/comments/{{commentId}}` | 특정 댓글 조회       |
| PATCH  | `/api/posts/comments/{{commentId}}` | 특정 댓글 수정       |
| DELETE | `/api/posts/comments/{{commentId}}` | 특정 댓글 삭제       |
| POST   | `/api/posts/{{postId}}/comments`    | 특정 게시글에 댓글 추가  |
| POST   | `/api/posts/{{postId}}/like`        | 특정 게시글에 좋아요 추가 |

</ul>
</div>
</details>

- 멀티미디어 (파일 업로드)
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드    | 엔드포인트                                             | 설명             |
|--------|---------------------------------------------------|----------------|
| GET    | `/api/multimedia?multiMediaId={{multiMediaId}}`   | 멀티미디어 조회       |
| POST   | `/api/multimedia?file={{file}}&postId={{postId}}` | 멀티미디어 파일 업로드   |
| DELETE | `/api/multimedia?multiMediaId={{multiMediaId}}`   | 멀티미디어 파일 삭제    |
| GET    | `/api/multimedia/search`                          | 멀티미디어 파일 검색    |
| GET    | `/api/multimedia/{{multiMediaId}}`                | 특정 멀티미디어 파일 조회 |

</ul>
</div>
</details>

### 🗨️ Chat Room

> 채팅방 생성 기능을 제공합니다.

- <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드  | 엔드포인트            | 설명     | 요청 본문 유형           |
|------|------------------|--------|--------------------|
| POST | `/api/chat/room` | 채팅방 생성 | `application/json` |

</ul>
</div>
</details>

### 🎁 Coupon & Promotion

> 쿠폰 및 프로모션 관리 기능을 제공합니다.

- 쿠폰
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드   | 엔드포인트                              | 설명          |
|-------|------------------------------------|-------------|
| GET   | `/api/my-coupons`                  | 내 쿠폰 목록 조회  |
| GET   | `/api/my-coupons/{{couponId}}`     | 특정 쿠폰 조회    |
| PATCH | `/api/my-coupons/{{couponId}}/use` | 특정 쿠폰 사용 처리 |
| POST  | `/api/coupons`                     | 쿠폰 생성       |
| POST  | `/api/coupons/{{couponId}}/issue`  | 특정 쿠폰 발행    |

</ul>
</div>
</details>

- 프로모션
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드    | 엔드포인트                             | 설명         |
|--------|-----------------------------------|------------|
| GET    | `/api/promotions`                 | 프로모션 목록 조회 |
| POST   | `/api/promotions`                 | 프로모션 생성    |
| GET    | `/api/promotions/{{promotionId}}` | 특정 프로모션 조회 |
| PUT    | `/api/promotions/{{promotionId}}` | 특정 프로모션 수정 |
| DELETE | `/api/promotions/{{promotionId}}` | 특정 프로모션 삭제 |

</ul>
</div>
</details>

### 📅 Time Slot, Reservation, Review, Report,  Grade

> 예약 시간 슬롯 리뷰, 신고 및 사용자 평가 항목 관리 기능(내부 시스템)을 제공합니다.

- 예약 시간 슬롯
  <details>
    <summary><b>EndPoint</b></summary>
      <div markdown="1">
        <ul>

| 메소드    | 엔드포인트                                                         | 설명          |
|--------|---------------------------------------------------------------|-------------|
| GET    | `/api/time-slots?startDate={{startDate}}&endDate={{endDate}}` | 시간 슬롯 목록 조회 |
| POST   | `/api/time-slots`                                             | 시간 슬롯 생성    |
| GET    | `/api/time-slots/{{timeSlotId}}`                              | 특정 시간 슬롯 조회 |
| PUT    | `/api/time-slots/{{timeSlotId}}`                              | 시간 슬롯 수정    |
| DELETE | `/api/time-slots/{{timeSlotId}}`                              | 시간 슬롯 삭제    |

</ul>
      </div>
    </details>

- 예약
  <details>
    <summary><b>EndPoint</b></summary>
      <div markdown="1">
        <ul>

| 메소드    | 엔드포인트                                        | 설명       |
|--------|----------------------------------------------|----------|
| GET    | `/api/reservations`                          | 예약 목록 조회 |
| POST   | `/api/reservations`                          | 예약 생성    |
| GET    | `/api/reservations/{{reservationId}}`        | 특정 예약 조회 |
| PUT    | `/api/reservations/{{reservationId}}`        | 예약 수정    |
| DELETE | `/api/reservations/{{reservationId}}`        | 예약 삭제    |
| PATCH  | `/api/reservations/{{reservationId}}/status` | 예약 상태 수정 |

</ul>
</div>
</details>

- 리뷰
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드    | 엔드포인트                       | 설명       |
|--------|-----------------------------|----------|
| GET    | `/api/reviews`              | 리뷰 목록 조회 |
| POST   | `/api/reviews`              | 리뷰 생성    |
| GET    | `/api/reviews/{{reviewId}}` | 특정 리뷰 조회 |
| PUT    | `/api/reviews/{{reviewId}}` | 리뷰 수정    |
| DELETE | `/api/reviews/{{reviewId}}` | 리뷰 삭제    |

</ul>
</div>
</details>

- 신고
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드    | 엔드포인트                           | 설명            |
|--------|---------------------------------|---------------|
| POST   | `/api/reports`                  | 신고 생성         |
| GET    | `/api/reports/users/{{userId}}` | 사용자에 대한 신고 조회 |
| GET    | `/api/reports/{{reportId}}`     | 특정 신고 조회      |
| DELETE | `/api/reports/{{reportId}}`     | 특정 신고 삭제      |

</ul>
</div>
</details>

- 평가항목관리정보 조회 (내부 시스템용)
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
  <ul>

| 메소드 | 엔드포인트                                                         | 설명                  |
|-----|---------------------------------------------------------------|---------------------|
| GET | `/api/grades/users/{{userId}}/reservations/{{reservationId}}` | 특정 예약에 대한 사용자 평점 조회 |
| GET | `/api/grades/users/{{userId}}/reviews/{{reviewId}}`           | 특정 리뷰에 대한 사용자 평점 조회 |

</ul>
</div>
</details>

### 💳 Payment & Settlement

> 결제 및 정산 관련 기능을 제공합니다.

- 결제
  <details>
    <summary><b>Payment Endpoints</b></summary>
    <div markdown="1">
    <ul>

| 메소드  | 엔드포인트                                                                                                                                   | 설명          |
|------|-----------------------------------------------------------------------------------------------------------------------------------------|-------------|
| GET  | `/api/payments/client-key`                                                                                                              | 클라이언트 키 조회  |
| GET  | `/api/payments/prepare`                                                                                                                 | 결제 준비       |
| POST | `/api/payments/toss`                                                                                                                    | 결제 처리       |
| GET  | `/api/payments/toss/fail?code={{$random.alphanumeric(8)}}&message={{$random.alphanumeric(8)}}&orderId={{$random.alphanumeric(8)}}`      | 결제 실패 처리    |
| GET  | `/api/payments/toss/fail-cancel?code={{$random.alphanumeric(8)}}&message={{$random.alphanumeric(8)}}`                                   | 결제 실패 취소 처리 |
| GET  | `/api/payments/toss/success?paymentKey={{$random.alphanumeric(8)}}&orderId={{$random.alphanumeric(8)}}&amount={{$random.integer(100)}}` | 결제 성공 처리    |
| POST | `/api/payments/toss/cancel?paymentKey={{$random.alphanumeric(8)}}&cancelReason={{$random.alphanumeric(8)}}`                             | 결제 취소 처리    |

</ul>
</div>
</details>

- 정산
  <details>
    <summary><b>Settlement Endpoints</b></summary>
    <div markdown="1">
    <ul>

| 메소드    | 엔드포인트                                                              | 설명       |
|--------|--------------------------------------------------------------------|----------|
| POST   | `/api/settlements`                                                 | 정산 생성    |
| GET    | `/api/settlements/details?settlementTime={{$random.integer(100)}}` | 정산 상세 조회 |
| GET    | `/api/settlements/search`                                          | 정산 검색    |
| GET    | `/api/settlements/test`                                            | 정산 테스트   |
| GET    | `/api/settlements/{{settlementId}}`                                | 특정 정산 조회 |
| PATCH  | `/api/settlements/{{settlementId}}`                                | 특정 정산 수정 |
| DELETE | `/api/settlements/{{settlementId}}`                                | 특정 정산 삭제 |

</ul>
</div>
</details>

## 💡 Trouble Shooting (작성 중)
