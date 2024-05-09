FROM amazoncorretto:17
EXPOSE 8080
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE=./buildtest/libs/thinktank-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 중괄호로 설정
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul", "app.jar"]
