package com.tk.gg.common.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                .allowedOriginPatterns("http://localhost:*") // 모든 출처 허용
                .allowedOrigins(
                        "http://13.209.24.74:19090",
                        "http://13.209.24.74:19091",
                        "http://13.209.24.74:19092",
                        "http://13.209.24.74:19093",
                        "http://13.209.24.74:19094",
                        "http://13.209.24.74:19095",
                        "http://13.209.24.74:19096",
                        "http://13.209.24.74:19097",
                        "http://13.209.24.74:19098",
                        "http://13.209.24.74:19099"
                ) // 모든 출처 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
