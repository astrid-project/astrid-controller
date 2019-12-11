AUTHOR Jalolliddin Yusupov [@yujboss](https://github.com/yujboss)

## Astrid Controller
Astrid Controller is  the core element of the ASTRID conceptual architecture, including both service
management and situational awareness. Starting from the descriptive and applicative semantics of the Context Model, controller is expected to deploy and manage the life-time of the service, by adapting the awareness layer of individual components and the whole service graph according to specific needs of detection algorithms. This means that monitoring operations, types and frequency of event reporting, level of logging is selectively and locally adjusted to retrieve the exact amount of knowledge, without overwhelming the whole system with unnecessary information. The purpose is to get more details for critical or vulnerable components when anomalies are detected that may indicate an attack, or when a warning is issued by cyber-security teams about new threats and vulnerabilities just discovered.
The supported actions, of astrid controller,  includes: set and change at run-time security configuration (logging, packet filtering rules), de-provision the service, add/replace/delete virtual functions in the service graph to remove vulnerabilities or compromised entities, divert traffic for legal interception. 



### Installing Controller via Maven (Spring Boot application with Embedded Tomcat)  [Solution 1] 
* install [jdk1.8.X YY](http://www.oracle.comntechnetwork/java/javase/downloads/jdk8-downloads-2133151.html);
* install [maven](https://maven.apache.org/install.html) 
* `mvn clean package`
* `java -jar target/controller-0.0.1-SNAPSHOT.jar`

## API Resources Design
| Resources     | URLs             | XML repr            | Meaning                                        |
|---------------|------------------|---------------------|------------------------------------------------|
| infrastucture | /insfrastructure | InfrastructureInfo  | XML file describing infrastructure information |
| policy        | /policy          |                     | yaml file describing the policy                |
| even          | /event           | InfrastructureEvent | XML file describing changes in the NFV         |
