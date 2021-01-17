
## Astrid Controller
Astrid Controller is  the core element of the ASTRID conceptual architecture, including both service
management and situational awareness. Starting from the descriptive and applicative semantics of the Context Model, controller is expected to deploy and manage the life-time of the service, by adapting the awareness layer of individual components and the whole service graph according to specific needs of detection algorithms. This means that monitoring operations, types and frequency of event reporting, level of logging is selectively and locally adjusted to retrieve the exact amount of knowledge, without overwhelming the whole system with unnecessary information. The purpose is to get more details for critical or vulnerable components when anomalies are detected that may indicate an attack, or when a warning is issued by cyber-security teams about new threats and vulnerabilities just discovered.
The supported actions, of astrid controller,  includes: set and change at run-time security configuration (logging, packet filtering rules), de-provision the service, add/replace/delete virtual functions in the service graph to remove vulnerabilities or compromised entities, divert traffic for legal interception. 



### Installing Controller via Maven (Spring Boot application with Embedded Tomcat)  [Solution 1] 
* install [jdk1.8.X YY](http://www.oracle.comntechnetwork/java/javase/downloads/jdk8-downloads-2133151.html);
* install [maven](https://maven.apache.org/install.html);
* [OPTIONAL] install [Apache Kafka](https://kafka.apache.org/quickstart) and running it;
* [OPTIONAL] install and running Neo4j database server (some details are written in the following);
* [OPTIONAL] check if your Neo4j database server is running; if is not, start it; 
* `mvn clean package`
* `java -jar target/controller-0.0.1-SNAPSHOT.jar`

##Configuration
Before to execute the Astrid Controller is necessary to configure in the Astrid-components.xml file (located in src/main/resources), which components have to communicate with Astrid Controller (like Verefoo modulo, Context Broker, Dashboard, exc). The syntax is the following:

	<component name='Verefoo' IPAddress='localhost' Port='8085'/>

##Integration with Verefoo module

The Astrid Controller works with Verefoo module to perform some actions.
It is possible to instance the Verefoo modulo inside the local machine where you execute the Astrid Controller (in this case you can follow this [steps](https://github.com/netgroup-polito/verefoo)), or you can use a remote instance of the Verefoo.

##Integration with Neo4j Database
This is an optional step, so the Astrid Controller works also without Neo4j database, but the functionality are limited.

The current version of Astrid-controller saves the InfrastructureEvents that receives throw InfrastructureEvent REST API into a Neo4j Database.

To install Neo4j server database, please following the guide that you can find [here](https://neo4j.com/docs/operations-manual/current/installation/).

There is already a Neo4j database setted in Astrid-components.xml file. After you install Neo4j check if the IP address and the port setted in that file is correct for your system. The syntax is the following:

	<component name='Astrid_DB' IPAddress='localhost' Port='7474' Username ='neo4j' Password='password'/>
	
The field 'name' must be 'Astrid_DB'. In the field 'Username' and 'Password' you should indicates the username and the password that you used to enter in database console.

Before you started the Astrid Controller, run Neo4j database with method in according to your O.S.

## Integration with Apache Kafka
The current version of Astrid-controller is listening to a Kafka topic with name 'testing-result'. Into this topic the Astrid-controller waits to receive the notifications of some attacks; for now the only attack that Astrid-controller is able to block is the DDoS LOICK attack.

The default configuration of Astrid Controller has the listening disable, but you can change it simply modifying the variable "listen.auto.kafka" into "application.properties" file that you can find to this path: [root]/src/main/resources.

If you enable the kafka listening into the application.properties file, after you installed the Apache Kafka on your machine, you should create the topic 'testing-result', as show [here](https://kafka.apache.org/quickstart) and before to run the Astrid-controller you should run the Apache Kafka.

## Context Broker Integration

For to execute its operations, the Security Controller need of Context Broker module, otherwise the Security Controller fails all its actions. You can set the parameter to reach a remote instance of it into the Astrid-components.xml, or you can run a local instance. If you want to follow the second choice, you can find all details [here](https://github.com/astrid-project/cb-manager).


## API Resources Design

The API integrated for now are the following:

| Resources     | Relative URLs              | Meaning                                        |
| ------------- | -------------------------- | ---------------------------------------------- |
| controller    | controller                 | The framework (main resource)                  |
| infrastucture | controller/insfrastructure | XML file describing infrastructure information |
| policy        | controller/policy          | yaml file describing the (reachability) policy |
| event         | controller /event          | XML file describing events occured in ASTRID   |

You can find more details and try them using the Swagger, that you can reachable to this URL: http://localhost:8083/controller/swagger-ui.html (after you launched the Astrid Controller).

|         Resource          | Verb  | Req.Body | Query params |                        Meaning                         |      Verb      | Resp.Body  |
| :-----------------------: | :---: | :------: | :----------: | :----------------------------------------------------: | :------------: | :--------: |
|        controller         |  GET  |          |              |                 Get the main resource                  |     200 OK     | Controller |
| controller/infrastructure | POST  |          |              | POST request correctly but without any or correct body | 415 No Content |   reason   |
| controller/infrastructure | POST  |          |              |      POST infrastructure information in valid XML      |     200 OK     |            |
|                           |       |          |              |                                                        |                |            |
|     controller/event      | POST  |          |              | POST request correctly but without any or correct body | 415 No Content |   reason   |
|     controller/event      | POST  |          |              |        POST event related to the security agent        |     200 OK     |            |
|                           |       |          |              |                                                        |                |            |
|     controller/policy     | POST  |          |              | POST request correctly but without any or correct body | 415 No Content |   reason   |
|     controller/policy     | POST  |          |              |  POST reachability policy from the Security Dashboard  |     200 OK     |            |

##Test
There are a specific class that testing the behavior of the Astrid Controller. The test performed are the following:

------ REST API - Resources: infrastructure (TEST CLASSES: TestRegisterInfrastructureAPI):

- Testing that send a GET request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a DELETE request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PUT request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PATCH request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a HEAD request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a OPTION request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a POST request correctly but without any body. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly without a correct body type. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly with a correct body type but non valid. The test passed if it receives a HTTP Status 400 (Bad Request);
- Testing that send a POST request correctly with a correct body. The test passed if it receives a HTTP Status 200 (Ok);

------ REST API - Resources: event (TEST CLASSES: TestRegisterPolicyAPI):

- Testing that send a GET request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a DELETE request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PUT request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PATCH request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a HEAD request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a OPTION request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a POST request correctly but without any body. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly without a correct body type. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly with a correct body. The test passed if it receives a HTTP Status 200 (Ok);

------ REST API - Resources: policy (TEST CLASSES: TestRegisterPolicyAPI):

- Testing that send a GET request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a DELETE request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PUT request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a PATCH request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a HEAD request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a OPTION request without body. The test passed if it receives a HTTP Status 405 (Method not allowed);
- Testing that send a POST request correctly but without any body. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly without a correct body type. The test passed if it receives a HTTP Status 415 (Unsupported Media Type);
- Testing that send a POST request correctly with a correct body type but non valid. The test passed if it receives a HTTP Status 400 (Bad Request);
- Testing that send a POST request correctly with a correct body. The test passed if it receives a HTTP Status 200 (Ok);


