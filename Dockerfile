# Sử dụng Java 17 làm base image
FROM eclipse-temurin:21-jdk-alpine

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file cấu hình Maven/Gradle (nếu có)

# Sao chép toàn bộ mã nguồn vào container

# Build ứng dụng Spring Boot
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests


# Xuất cổng 8080 (hoặc cổng bạn đã cấu hình)
EXPOSE 22986

# Cấu hình biến môi trường
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
ENV SERVER_PORT=22986

# Lệnh để chạy ứng dụng Spring Boot
CMD ["java", "-jar", "target/*.jar"]