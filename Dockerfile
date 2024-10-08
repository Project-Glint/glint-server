# 1. Build stage - Gradle 빌드를 위한 단계
FROM gradle:7.6.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 설정 파일 복사 (필요한 파일만 복사)
COPY gradle/wrapper gradle/wrapper
COPY gradlew gradlew
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies --no-daemon || return 0


# 나머지 소스 코드 복사
COPY src src

# 프로젝트 빌드 (테스트는 제외하여 빌드 속도 최적화)
RUN ./gradlew bootJar --no-daemon -x test

# 2. Runtime stage - 경량화된 이미지로 최종 JAR 실행
FROM openjdk:17-jdk-slim AS runner

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 실행할 포트 명시
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
