# 1. Aşama: Build (Maven ile projeyi derle)
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
# Testleri atlayarak build alıyoruz (hız kazanmak için)
RUN ./mvnw clean package -DskipTests

# 2. Aşama: Run (Sadece oluşan JAR dosyasını çalıştır)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Build aşamasından çıkan JAR dosyasını kopyala
COPY --from=build /app/target/cftcbrandtech-0.0.1-SNAPSHOT.jar app.jar

# Uygulamanın çalışacağı port
EXPOSE 8080

# Başlatma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]