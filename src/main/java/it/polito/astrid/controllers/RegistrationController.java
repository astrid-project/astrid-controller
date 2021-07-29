package it.polito.astrid.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.String;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.astrid.jaxb.Components;
import it.polito.verefoo.astrid.jaxb.Components.*;
import it.polito.verefoo.jaxb.NFV;
import net.minidev.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;
import it.polito.astrid.models.Configuration;
import it.polito.astrid.models.Exec;
import it.polito.astrid.models.InterceptionRequest;
import it.polito.astrid.models.KafkaMessage;
import it.polito.astrid.models.NetworkStatus;
import it.polito.astrid.service.DroolsService;
import it.polito.astrid.service.RegisterService;

//mvn clean package && java -jar target\controller-0.0.1-SNAPSHOT.jar

@Controller
@RequestMapping("/")
public class RegistrationController {

	public static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	// private final KafkaConsumerConfig kafkaService;
	private final DroolsService droolsService;
	private final RegisterService registerService;

	public Components AstridComponents;

	public RegistrationController() throws AstridComponentNotFoundException {
		if(System.getenv("KAFKA_BOOTSTRAP_SERVER")!=null) {
				System.setProperty("spring.kafka.screescrbootstrap-servers", System.getenv("KAFKA_BOOTSTRAP_SERVER"));
				logger.info("-------> Kafka IPs " + System.getenv("KAFKA_BOOTSTRAP_SERVER"));
	    }else {
                logger.info("-------> Kafka IP was not set " + System.getenv("KAFKA_BOOTSTRAP_SERVER"));
	    	System.setProperty("spring.kafka.bootstrap-servers", "192.168.31.200:9092");
	    }
		droolsService = new DroolsService();
		registerService = new RegisterService();
		// kafkaService = new KafkaConsumerConfig(droolsService);
		AstridComponents = null;

		/* verify if the XML file with configuration of ASTRID components is valid */
		boolean validation = validateXML("src/main/resources/Astrid-components.xml", "xsd/Astrid-components.xsd");
		if (validation != true) {
			logger.info("The XML file with configuration of ASTRID components, it is NOT valid");
		} else {
			/* upload the ASTRID component */
			try {
				InputStream inStream = new FileInputStream("src/main/resources/Astrid-components.xml");
				JAXBContext jaxbContext = JAXBContext.newInstance(Components.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				AstridComponents = (Components) unmarshaller.unmarshal(inStream);
			} catch (JAXBException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(System.getenv("CB_IP")!=null && System.getenv("CB_PORT") !=null) {
			registerService.getContextBroker(this).setIPAddress(System.getenv("CB_IP"));
			registerService.getContextBroker(this).setPort(new BigInteger(System.getenv("CB_PORT")));
			logger.info("-------> ContextBroker IPs " + System.getenv("CB_IP")+" "+System.getenv("CB_PORT"));
		}
	}
	
	
	@ApiOperation(value = "firewallRules", notes = "Recieves firewallRules  sends it to CB. Waits for result and sends it back", response = NFV.class)
	@RequestMapping(method = RequestMethod.POST, value = "/register/firewallRules", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<String> firewallRules(
			@ApiParam(value = "Infrastructure Info", required = true) @RequestBody JSONObject  firewall) throws AstridComponentNotFoundException {
		registerService.setComponent(registerService.getContextBroker(this));
		
	    return registerService.passthroughFW(firewall);
	}
	
	@ApiOperation(value = "firewallRules", notes = "Recieves firewallRules  sends it to CB. Waits for result and sends it back", response = NFV.class)
	@RequestMapping(method = RequestMethod.GET, value = "/register/firewallRules", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<String> getFirewallRules() throws AstridComponentNotFoundException {
		registerService.setComponent(registerService.getContextBroker(this));
		
	    return registerService.passthroughFWget();
	}
	
	
	@RequestMapping(value = "/removeExec", method = RequestMethod.GET)
	@ResponseBody
	public void removeExecEnvs() throws ContextBrokerException, AstridComponentNotFoundException {
		registerService.setComponent(registerService.getContextBroker(this));
		registerService.removeExecEnvs();
	}
	@RequestMapping(value = "/removeDyn", method = RequestMethod.GET)
	@ResponseBody
	public void removeDynMon() throws ContextBrokerException, AstridComponentNotFoundException {
		registerService.setComponent(registerService.getContextBroker(this));
		registerService.removeDynMon();
	}

	@ApiOperation(value = "registerInfrastructure", notes = "Recieves Infrastructure info and sends it to Verikube. Waits for result and sends it back", response = NFV.class)
	@RequestMapping(method = RequestMethod.POST, value = "/register/insfrastructure", produces = "application/xml", consumes = "application/xml")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerInfrastructure(
			@ApiParam(value = "Infrastructure Info", required = true) @RequestBody InfrastructureInfo info)
			throws ResourceNotFoundException, AstridComponentNotFoundException {
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/insfrastructure", info, null);
		droolsService.sendInterceptionRequest(IR, registerService.getVerefooInfo(this), null, null);
		return IR.getNfv();
	}

	@ApiOperation(value = "registerPolicy", notes = "Recieves Policies as String and sends it to Verikube. ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/policy", produces = "text/plain", consumes = "text/plain")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerPolicy(@RequestBody String policy)
			throws AstridComponentNotFoundException, IOException, ResourceNotFoundException {
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/policy", null, policy);
		droolsService.sendInterceptionRequest(IR, registerService.getVerefooInfo(this), registerService.getContextBroker(this), null);
		return IR.getNfv();
	}

	@ApiOperation(value = "registerDeployement", notes = "Recieves deployement . ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/deployement", produces = "text/plain", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<String> registerDeployment(@RequestBody Configuration config)
			throws AstridComponentNotFoundException, IOException, ResourceNotFoundException {
		registerService.setComponent(registerService.getContextBroker(this));
		
		String result = registerService.registerDeployment(config);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "createExecEnv", notes = "Recieves ExecEnv . ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/execenv", produces = "text/plain", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<Exec> registerExecEnv(@RequestBody Exec exec)
			throws AstridComponentNotFoundException, ResourceNotFoundException, InterruptedException {
		registerService.setComponent(registerService.getContextBroker(this));
		try {
			registerService.registerExec(exec);
			File folder = new File("./src/main/resources/code");
			System.out.println("#### "+folder.listFiles().length);
			for (File iterable_element : folder.listFiles()) {
				if (iterable_element.isFile()) {
					String fileString = new String(Files.readAllBytes(Paths.get(iterable_element.getCanonicalPath())), StandardCharsets.UTF_8);
					//registerService.uploadToCatalog(fileString);
				}
			}
		} catch (java.io.IOException e) {
			System.out.println("___> "+e);
		}
		TimeUnit.SECONDS.sleep(10);
		
		try {
			uploadInitialBau("DNS");	
		} catch (Exception e) {
		}
		
		//uploadInitialBau("NTP");

		return new ResponseEntity<Exec>(exec, HttpStatus.OK);
	}



	@ApiOperation(value = "registerEvent", notes = "Recieves an Event and sends it to Verikube. ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/event", produces = "application/xml", consumes = "application/xml")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerEvent(@RequestBody InfrastructureEvent event)
			throws ResourceNotFoundException, AstridComponentNotFoundException {
		// String userId, String providerId, String serviceId, String command,
		// InfrastructureInfo info, String policy
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/event", null, null);
		IR.setEvent(event);
		droolsService.sendInterceptionRequest(IR, registerService.getVerefooInfo(this), registerService.getAstridDB(this), registerService.getContextBroker(this));
		return IR.getNfv();
	}

	private boolean validateXML(String pathOfXML, String pathOfXSD) {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(pathOfXSD));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(pathOfXML)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.info("XML file is NOT valid: " + e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Unable to validate Astrid-components.XML: " + e.getMessage());
		}
		return true;
	}

	@RequestMapping(method = RequestMethod.OPTIONS, value = "*/*")
	@ApiIgnore
	public void captureOPTIONRequest(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value(), "Request method 'OPTIONS' not supported");
	}

	@ExceptionHandler({ IOException.class })
	public void handleIOException(HttpServletResponse httpRes, Throwable ex) throws IOException {
		httpRes.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), "Unable to contact external module");
	}

	@ExceptionHandler({ AstridComponentNotFoundException.class })
	public void handleAstridComponentNotFoundException(HttpServletResponse httpRes, Throwable ex) throws IOException {
		httpRes.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), ex.getMessage());
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public void handleResourceNotFoundException(HttpServletResponse httpRes, Throwable ex) throws IOException {
		httpRes.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	}

	@KafkaListener(topics = "testing-result", autoStartup = "${listen.auto.kafka}")
	public void listen(ConsumerRecord<?, ?> cr) throws Exception {
		// receive a message on kafka bus
		ObjectMapper objectMapper = new ObjectMapper();
		KafkaMessage mess = objectMapper.readValue(cr.value().toString().toLowerCase(), KafkaMessage.class);
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "kafka", null, null);
		IR.setMess(mess);
		droolsService.sendInterceptionRequest(IR, registerService.getContextBroker(this), null, null);
		logger.info("++++++++++ Receive testing-result Kafka Messages: " + cr.value().toString());
	}

	
	private void uploadInitialBau(String bau) throws AstridComponentNotFoundException {
		KafkaMessage mess = new KafkaMessage();
		NetworkStatus status = new NetworkStatus();
		status.setmUseCase(bau);
		status.networkStatus="BAU";
		mess.setStatus(status);

		InterceptionRequest IR = new InterceptionRequest(null, null, null, "kafka", null, null);
		IR.setMess(mess);
		System.out.println("____ Uploading initial BAUs ");
		System.out.println("##### " + status.networkStatus + " " + status.getmUseCase());
		droolsService.sendInterceptionRequest(IR, registerService.getContextBroker(this), null, null);
	}
	
	@KafkaListener(topics = "AstridProxyPublishStatus", autoStartup = "${listen.auto.kafka}")
	public void listen2(ConsumerRecord<?, ?> cr) {
		String inputS = cr.value().toString();
		logger.info("++++++++++ Receive Status Kafka Messages:         " + cr.value().toString());
		logger.info("            ++++++++++ ");
		inputS = inputS.replaceAll("^\"|\"$", "");

		String unescaped = StringEscapeUtils.unescapeJson(inputS);

		ObjectMapper objectMapper = new ObjectMapper();
		NetworkStatus status = null;
		try {

			status = objectMapper.readValue(unescaped, NetworkStatus.class);
			logger.info("            ++++++++++ ");

			if (status.networkStatus.equals("WARN") || status.networkStatus.equals("BAU")
					|| status.networkStatus.equals("ALARM")) {
				status.setmUseCase(status.correlationTypeStatus.get(0).getUseCase());

			}
			System.out.println("##### " + status.networkStatus + " " + status.getmUseCase());
			KafkaMessage mess = new KafkaMessage();
			mess.setStatus(status);

			InterceptionRequest IR = new InterceptionRequest(null, null, null, "kafka", null, null);
			IR.setMess(mess);
			droolsService.sendInterceptionRequest(IR, registerService.getContextBroker(this), null, null);

		} catch (IOException e) {
			logger.info("   Error in parsing Json");
			// e.printStackTrace();
		} catch (AstridComponentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@KafkaListener(topics = "Status", autoStartup = "${listen.auto.kafka}")
	public void listen3(ConsumerRecord<?, ?> cr) throws Exception {
		logger.info("++++++++++ Receive LEA Kafka Messages: " + cr.value().toString());
	}
}
