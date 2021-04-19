#
# Build stage
#
FROM maven:3.5-jdk-8 as mavenDeps
COPY src /home/app/src
COPY gen-src /home/app/gen-src
COPY xsd /home/app/xsd
COPY pom.xml /home/app
WORKDIR /home/app


#
# Package stage
#
   
RUN mvn clean package
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/controller-0.0.1-SNAPSHOT.jar"]
