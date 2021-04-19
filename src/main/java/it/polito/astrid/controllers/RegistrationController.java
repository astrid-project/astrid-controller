package it.polito.astrid.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.lang.String;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.stream.StreamSource;

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
import springfox.documentation.annotations.ApiIgnore;
import it.polito.astrid.models.InterceptionRequest;
import it.polito.astrid.models.KafkaMessage;
import it.polito.astrid.service.DroolsService;

//mvn clean package && java -jar target\controller-0.0.1-SNAPSHOT.jar

@Controller
@RequestMapping("/")
public class RegistrationController {
	
	private static final Logger logger=LoggerFactory.getLogger(RegistrationController.class);
	//private final KafkaConsumerConfig kafkaService;
	private final DroolsService droolsService;
	
	private Components AstridComponents;
	
	public RegistrationController() {
		droolsService = new DroolsService();
		//kafkaService = new KafkaConsumerConfig(droolsService);
		AstridComponents = null;
		
		/* verify if the XML file with configuration of ASTRID components is valid*/
		boolean validation = validateXML("src/main/resources/Astrid-components.xml", "xsd/Astrid-components.xsd");
		if(validation != true) {
			logger.info("The XML file with configuration of ASTRID components, it is NOT valid");
		}else {
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
	}

	@ApiOperation(value = "registerInfrastructure", notes = "Recieves Infrastructure info and sends it to Verikube. Waits for result and sends it back", response = NFV.class)
	@RequestMapping(method = RequestMethod.POST, value = "/register/insfrastructure", produces = "application/xml", consumes = "application/xml")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
	@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerInfrastructure(
			@ApiParam(value = "Infrastructure Info", required = true) @RequestBody InfrastructureInfo info) throws ResourceNotFoundException, AstridComponentNotFoundException {
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/insfrastructure", info, null);
		droolsService.sendInterceptionRequest(IR, getVerefooInfo(), null, null);
		return IR.getNfv();
	}

	@ApiOperation(value = "registerPolicy", notes = "Recieves Policies as String and sends it to Verikube. ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/policy", produces = "text/plain", consumes = "text/plain")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerPolicy(@RequestBody String policy) throws AstridComponentNotFoundException, IOException, ResourceNotFoundException {
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/policy", null, policy);
		droolsService.sendInterceptionRequest(IR, getVerefooInfo(), getContextBroker(), null);
		return IR.getNfv();
	}
	
	@ApiOperation(value = "registerEvent", notes = "Recieves an Event and sends it to Verikube. ")
	@RequestMapping(method = RequestMethod.POST, value = "/register/event", produces = "application/xml", consumes = "application/xml")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@ResponseBody
	public ResponseEntity<NFV> registerEvent(@RequestBody InfrastructureEvent event) throws ResourceNotFoundException, AstridComponentNotFoundException {
		InterceptionRequest IR = new InterceptionRequest(null, null, null, "/register/event", null, null);
		IR.setEvent(event);
		droolsService.sendInterceptionRequest(IR, getVerefooInfo(), getAstridDB(), getContextBroker());
		return IR.getNfv();
	}
	
	private Component getVerefooInfo() throws AstridComponentNotFoundException {
		if (AstridComponents == null) {
			logger.error("There aren't ASTRID components configure.");
			throw new AstridComponentNotFoundException("Unable to contact external module");
		}
		Iterator<Component> iter = AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) { 
			c = iter.next();
            if (c.getName().equals("Verefoo"))
            	return c;
        }
		logger.error("There isn't any Verefoo component configured.");
		throw new AstridComponentNotFoundException("Unable to contact external module");
	}
	
	private Component getContextBroker() throws AstridComponentNotFoundException {
		if (AstridComponents == null) {
			logger.error("There aren't ASTRID components configure.");
			throw new AstridComponentNotFoundException("Unable to contact external module");
		}
		Iterator<Component> iter = AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) { 
			c = iter.next();
            if (c.getName().equals("ContextBroker"))
            	return c;
        }
		logger.error("There isn't any Context Broker component configured.");
		throw new AstridComponentNotFoundException("Unable to contact external module");
	}
	
	private boolean validateXML(String pathOfXML, String pathOfXSD){
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(pathOfXSD));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(pathOfXML)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.info("XML file is NOT valid: "+e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Unable to validate Astrid-components.XML: "+e.getMessage());
		}
		return true;
	}
	
	private Component getAstridDB(){
		if (AstridComponents == null) {
			logger.info("There aren't ASTRID components configure.");
			Component C = new Component();
			C.setName("Astrid_DB");
			C.setIPAddress("0.0.0.0");
			return C;
		}
		Iterator<Component> iter = AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) { 
			c = iter.next();
            if (c.getName().equals("Astrid_DB"))
            	return c;
        } 
		logger.info("There isn't any Astrid Database configured.");
		Component C = new Component();
		C.setName("Astrid_DB");
		C.setIPAddress("0.0.0.0");
		return C;
	}
	
	@RequestMapping(method = RequestMethod.OPTIONS, value = "*/*")
	@ApiIgnore
	public void captureOPTIONRequest(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value(), "Request method 'OPTIONS' not supported");
	}
	
	 @ExceptionHandler({IOException.class})
	 public void handleIOException(HttpServletResponse httpRes,Throwable ex) throws IOException{
		 httpRes.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), "Unable to contact external module");
	    }
	 
	 @ExceptionHandler({AstridComponentNotFoundException.class})
	 public void handleAstridComponentNotFoundException(HttpServletResponse httpRes,Throwable ex) throws IOException{
		 httpRes.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), ex.getMessage());
	    }
	 
	 @ExceptionHandler({ResourceNotFoundException.class})
	 public void handleResourceNotFoundException(HttpServletResponse httpRes,Throwable ex) throws IOException{
		 httpRes.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	    }
	 
	 @KafkaListener(topics = "testing-result", autoStartup = "${listen.auto.kafka}")
	 public void listen(ConsumerRecord<?, ?> cr) throws Exception {
		 //receive a message on kafka bus
		 ObjectMapper objectMapper = new ObjectMapper();
		 KafkaMessage mess = objectMapper.readValue(cr.value().toString().toLowerCase(), KafkaMessage.class);
		 InterceptionRequest IR = new InterceptionRequest(null, null, null, "kafka", null, null);
		 IR.setMess(mess);
		 droolsService.sendInterceptionRequest(IR, getContextBroker(), null, null);
		 logger.info("++++++++++ Receive testing-result Kafka Messages: " + cr.value().toString());
	 }

	 @KafkaListener(topics = "Status", autoStartup = "${listen.auto.kafka}")
	 public void listen2 (ConsumerRecord<?, ?> cr) throws Exception {
		 logger.info("++++++++++ Receive Status Kafka Messages: " + cr.value().toString());
	 }
	 
	 @KafkaListener(topics = "Status", autoStartup = "${listen.auto.kafka}")
	 public void listen3 (ConsumerRecord<?, ?> cr) throws Exception {
		 logger.info("++++++++++ Receive LEA Kafka Messages: " + cr.value().toString());
	 }
}