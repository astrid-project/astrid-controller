#
# Build stage
#
FROM maven:3.5-jdk-8 as mavenDeps
COPY src /home/app/src
COPY gen-src /home/app/gen-src
COPY gen-src-astrid /home/app/gen-src-astrid
COPY xsd /home/app/xsd
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM maven:3.5-jdk-8 as mavenDeps   
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/controller-0.0.1-SNAPSHOT.jar"]
