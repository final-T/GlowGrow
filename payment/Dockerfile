# Stage 1: Build the application
FROM gradle:8.10.1-jdk17 AS build

WORKDIR /app

# ARG로 전달된 파일 디렉터리 설정
ARG FILE_DIRECTORY

# 파일 복사
COPY $FILE_DIRECTORY /app

# Payment 모듈만 빌드
RUN ./gradlew :payment:clean :payment:build -x test --no-daemon

FROM openjdk:17-jdk-slim

COPY --from=build /app/payment/build/libs/*SNAPSHOT.jar /app.jar

# JAR 파일 실행
CMD ["java", "-jar", "/app.jar"]