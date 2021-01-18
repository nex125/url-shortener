# the first stage
FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn clean package

# the second stage of our build
FROM openjdk:8-jre-alpine3.9

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/url-shortener-0.1.jar /url-shortener.jar

# startup command to execute the jar
CMD ["java", "-jar", "/url-shortener.jar"]
