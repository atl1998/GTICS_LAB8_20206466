FROM openjdk:23-ea-17-jdk
VOLUME /temp
EXPOSE 8080
ADD ./target/lab8_20206466-0.0.1-SNAPSHOT.jar lab8.jar
ENTRYPOINT ["java", "-jar","lab8.jar"]