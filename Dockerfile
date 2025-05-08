# 1) Build aşaması
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# 2) Run aşaması
FROM eclipse-temurin:17-jre

# Uygulamanın çalışacağı klasörü /app yapıyoruz
WORKDIR /app

# build aşamasındaki JAR'ı WORKDIR içine, app.jar adıyla kopyala
COPY --from=build /workspace/target/*.jar ./app.jar

# port ayarı
EXPOSE 8080

# Çalıştırma komutu; WORKDIR=/app olduğu için app.jar otomatik bulunacak
ENTRYPOINT ["java","-jar","app.jar"]
