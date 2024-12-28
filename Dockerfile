FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /home/app
ADD . /home/app/slideshow-management
RUN cd slideshow-management && mvn clean package

FROM maven:3.8.3-openjdk-17 as optimizer

WORKDIR /home/app
COPY --from=builder /home/app/slideshow-management/app/target/slideshow-management.jar slideshow-management.jar
RUN java -Djarmode=layertools -jar slideshow-management.jar extract

FROM eclipse-temurin:17-jre-focal

COPY --from=optimizer /home/app/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-loader/ ./
COPY --from=optimizer /home/app/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

