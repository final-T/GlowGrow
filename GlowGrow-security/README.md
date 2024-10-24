## GlowGrow Security 사용법
> Spring Security 기반의 인증 모듈입니다.
### 구현 항목
- JwtProvider: Jwt 관련 유틸입니다.
- SecurityFilterChain & Config: 인가 관련한 필터체인 및 설정입니다. (따로 수정할 필요가 없습니다.)
- AuthUser with MethodArgumentResolver: 인증된 유저의 정보를 Controller에서 받을 수 있습니다.
- SecurityRequestMatcherChain & SecurityRequestMatcher: 인가를 핸들링할 객체 입니다. (빈 등록이 꼭 필요합니다)

### 작동 방식
- 구현된 Security 설정들은 Spring Context에 등록됩니다. (Config / MethodArgumentResolver / JwtProvider)
- SecurityMatcherChain에서 등록된 SecurityRequestMatcher들을 인가 URL로 설정합니다.

### 사용법 - Dependency와 인증
- `build.gradle`에 `implementation`으로 dependency 설정
- `@SpringBootApplication(scanBasePackages = {..})`에 `"com.tk.gg"` 혹은 `"com.tk.gg.security" 추가`
- 
### 사용법 - SecurityRequestMatcherChain (필수)
> 해당 Config파일이 등록되지 않을 경우 Exception 발생
>

아래의 Config 설정을 필요에 따라 설정해주세요.
```java
@Configuration
public class CustomSecurityConfig {
    @Bean
    public SecurityRequestMatcherChain securityRequestMatcherChain() {
        SecurityRequestMatcherChain securityRequestMatcherChain = new SecurityRequestMatcherChain();

        securityRequestMatcherChain
            .add(SecurityRequestMatcher.hasRoleOf(UserRole.MASTER, "/test"));

        return securityRequestMatcherChain;
    }
}
```
### 사용법 @AuthUser
인가가 완료된(Security 필터를 무사히 통과) 경우,
`@AuthUser`와 `AuthUserInfo` 인터페이스를 활용하여 인증된 유저 정보를 가져올 수 있습니다.   
가져올 수 있는 정보는 `AuthUserInfo`를 확인해주세요.

> 만약 인증이 없는 API에 쓴 경우 혹은 인증에 문제가 생긴 경우 null로 설정됩니다.

```java
@GetMapping("/test")
@Operation(summary = "인증 & 인가 테스트", description = "인가 테스트를 위한 API입니다. Jwt 정보를 파싱해서 리턴합니다. (해당 API는 ADMIN 권한만 access 가능합니다.)",
    security = @SecurityRequirement(name = "bearerAuth"))
public GlobalResponse<TokenableUser> test(@AuthUser TokenableUser user) {
    return ApiUtils.success("인가 테스트 성공", user);
}
```
### 기타 정보
- 인증은 User Application에서 진행됩니다. 인증이 필요한 경우 User Application Server를 켜주세요
- Jwt 토큰의 만료기간은 AccessToken은 15분, RefreshToken은 30일입니다.
- 재발급 받은 RefreshToken은 더 이상 사용할 수 없습니다.

### 주의 사항
> SecurityRequestMatcherChain을 꼭 빈으로 등록