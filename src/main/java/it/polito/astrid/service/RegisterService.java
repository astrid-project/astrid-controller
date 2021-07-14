package it.polito.astrid.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.astrid.controllers.AstridComponentNotFoundException;
import it.polito.astrid.controllers.ContextBrokerException;
import it.polito.astrid.controllers.RegistrationController;
import it.polito.astrid.models.Exec;
import it.polito.astrid.models.Exec.Node;
import it.polito.astrid.models.Configuration.Agent;
import it.polito.contextbroker.model.Execution_Environment;
import it.polito.contextbroker.model.Execution_Environment.LCP;
import it.polito.verefoo.astrid.jaxb.Components.Component;
import net.minidev.json.JSONObject;

public class RegisterService {
	private Component ContextBroker;
	private static final Logger logger=LoggerFactory.getLogger(RegisterService.class);
	String prefix = "sc-ebpf-";
	public RegisterService() {
	}
	
	public String registerExec( Exec execs) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		
		RestTemplate restTemplate = new RestTemplate();
		
		int uid = generateUniqueId();
		List<Execution_Environment> execList = new ArrayList<Execution_Environment>();
		for (Node name : execs.getExecenvs().getNodes()) {
			Execution_Environment exec = new Execution_Environment();
			Execution_Environment.LCP  lcp = exec.new LCP();
			lcp.setPort(execs.getExecenvs().getLcp());
			lcp.setHttps(false);
			exec.setLcp(lcp);
			exec.setDescription("polycube for ebpf "+uid+" "+name.getId());
			exec.setId(prefix+uid+" "+name.getId());
			exec.setType_id("container-docker");
			exec.setHostname( name.getId()+"."+execs.getExecenvs().getNamespace());
			exec.setEnabled( "Yes");
			execList.add(exec);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(mapper.writeValueAsString(execList), headers);
		
		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/exec-env", requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ Execution Enviroment created with id = "+prefix+uid);
			}else {
				logger.error("++++++++++ Execution Enviroment  with an error: " + result.getBody());
			}
		}catch(Exception e) {
			logger.error("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
		return "sc-ebpf-"+uid;
	}

	 public static int generateUniqueId() {      
	        UUID idOne = UUID.randomUUID();
	        String str=""+idOne;        
	        int uid=str.hashCode();
	        String filterStr=""+uid;
	        str=filterStr.replaceAll("-", "");
	        return Integer.parseInt(str);
	    }

	public void setComponent(Component contextBroker) {
		this.ContextBroker=contextBroker;
		
	}
	private static String usingBufferedReader(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
 
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) 
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

	public void uploadToCatalog(String fileLocation) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
	
		RestTemplate restTemplate = new RestTemplate();
		
		
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(fileLocation, headers);
		
		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/catalog/ebpf-program", requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ EBPF loaded created with file = "+fileLocation);
			}else {
				logger.error("++++++++++ Execution Enviroment  with an error: " + result.getBody());
			}
		}catch(Exception e) {
			logger.error("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
	}

	public ResponseEntity<String> passthroughFW(JSONObject firewall) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
	
		RestTemplate restTemplate = new RestTemplate();
		
		// Data attached to the request.
		HttpEntity<JSONObject> requestBody = new HttpEntity<JSONObject>(firewall, headers);
		System.out.println("$$$$$$$$$$$$$$$$ "+requestBody.toString());
		ResponseEntity<String> result = null;
			// Send request with POST method.
		result = restTemplate.exchange("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent", HttpMethod.PUT,requestBody, String.class);
		logger.info("++++++++++ Result = "+result);
		if (result.getStatusCode() == HttpStatus.OK) {
			logger.info("++++++++++ JSON FW passed = "+result.getBody());
		}else {
			logger.error("++++++++++ JSON FW not passed  with an error: " + result.getBody());
			return result;
		}
		return result;
	}

	public void removeExecEnvs() throws ContextBrokerException {
		List<Execution_Environment> exec_env = new ArrayList<>();
		exec_env = getExecutionEnvironment();
		
		logger.info("+++++++++ Exec-envs  = "+exec_env.size());
		Iterator<Execution_Environment> it = exec_env.iterator();
		boolean trovato = false;
		// recovery the id of exec_env that has the IP egual to source IP of the kafka
		// message
		while (it.hasNext() && trovato == false) {
			Execution_Environment node = it.next();
			logger.info("############# Looking for exec "+node.getId());
			if(node.getId().startsWith("sc-ebpf")) {
				logger.info("+++++++++ Found exec-env "+node.getId());
				removeExecPost(node.getId());
			}
		}
		
	}
	
	private void removeExecPost(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID "
				+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		RestTemplate restTemplate = new RestTemplate();
		// Data attached to the request.
		HttpEntity<?> requestBody = new HttpEntity<Object>(headers);
		ResponseEntity<String> result = null;
		String urlA = "http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort()
		+ "/exec-env/" + id;
		result = restTemplate.exchange(urlA, HttpMethod.DELETE, requestBody, String.class);
		if (result.getStatusCode() == HttpStatus.OK) {
			logger.info("+++++++++  exec-env succesfully removed"+id);
		}
	}

	private List<Execution_Environment> getExecutionEnvironment() throws ContextBrokerException {
		List<Execution_Environment> exec_env = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID "
				+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		HttpEntity<?> requestBody = new HttpEntity<Object>(headers);
		
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		try {
			logger.error("_________ "+ContextBroker.getIPAddress()+ContextBroker.getPort() +" "+requestBody);
			ResponseEntity<String> response = restTemplate.exchange(
					"http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/exec-env",
					HttpMethod.GET, requestBody, String.class, 1);
			logger.error(response.getStatusCode()+response.getBody());
			exec_env = Arrays.asList(objectMapper.readValue(response.getBody(), Execution_Environment[].class));
		} catch (Exception e) {
			logger.error(
					"+++++++++ error while asking the execution environments to Context Broker: " + e);
			throw new ContextBrokerException(e.getMessage());
		}
		return exec_env;
	}

	public Component getVerefooInfo(RegistrationController registrationController) throws AstridComponentNotFoundException {
		if (registrationController.AstridComponents == null) {
			RegistrationController.logger.error("There aren't ASTRID components configure.");
			throw new AstridComponentNotFoundException("Unable to contact external module");
		}
		Iterator<Component> iter = registrationController.AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) {
			c = iter.next();
			if (c.getName().equals("Verefoo"))
				return c;
		}
		RegistrationController.logger.error("There isn't any Verefoo component configured.");
		throw new AstridComponentNotFoundException("Unable to contact external module");
	}

	public Component getAstridDB(RegistrationController registrationController) {
		if (registrationController.AstridComponents == null) {
			RegistrationController.logger.info("There aren't ASTRID components configure.");
			Component C = new Component();
			C.setName("Astrid_DB");
			C.setIPAddress("0.0.0.0");
			return C;
		}
		Iterator<Component> iter = registrationController.AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) {
			c = iter.next();
			if (c.getName().equals("Astrid_DB"))
				return c;
		}
		RegistrationController.logger.info("There isn't any Astrid Database configured.");
		Component C = new Component();
		C.setName("Astrid_DB");
		C.setIPAddress("0.0.0.0");
		return C;
	}

	public Component getContextBroker(RegistrationController registrationController) throws AstridComponentNotFoundException {
		if (registrationController.AstridComponents == null) {
			RegistrationController.logger.error("There aren't ASTRID components configure.");
			throw new AstridComponentNotFoundException("Unable to contact external module");
		}
		Iterator<Component> iter = registrationController.AstridComponents.getComponent().iterator();
		Component c;
		while (iter.hasNext()) {
			c = iter.next();
			if (c.getName().equals("ContextBroker"))
				return c;
		}
		RegistrationController.logger.error("There isn't any Context Broker component configured.");
		throw new AstridComponentNotFoundException("Unable to contact external module");
	}

	public ResponseEntity<String> passthroughFWget() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
	
		RestTemplate restTemplate = new RestTemplate();
		
		// Data attached to the request.
		HttpEntity<?> requestBody = new HttpEntity<Object>(headers);
		ResponseEntity<String> result = null;
			// Send request with POST method.
		result = restTemplate.exchange("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent", HttpMethod.GET,requestBody, String.class);
		logger.info("++++++++++ Result = "+result);
		if (result.getStatusCode() == HttpStatus.OK) {
			logger.info("++++++++++ JSON FW passed = "+result.getBody());
		}else {
			logger.error("++++++++++ JSON FW not passed  with an error: " + result.getBody());
			return result;
		}
		return result;
	}

	

}
