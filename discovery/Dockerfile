FROM openjdk:17
EXPOSE 8889
ADD target/discovery-0.0.1-SNAPSHOT.jar discovery.jar
ENTRYPOINT ["java", "-jar", "discovery.jar"]