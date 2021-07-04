package it.polito.astrid.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.astrid.models.Exec;
import it.polito.astrid.models.Exec.Node;
import it.polito.astrid.models.Configuration.Agent;
import it.polito.contextbroker.model.Execution_Environment;
import it.polito.contextbroker.model.Execution_Environment.LCP;
import it.polito.verefoo.astrid.jaxb.Components.Component;

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
	
	


}
