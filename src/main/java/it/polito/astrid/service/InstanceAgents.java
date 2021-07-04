package it.polito.astrid.service;

import java.io.IOException;

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

import it.polito.astrid.models.Configuration;
import it.polito.astrid.models.Configuration.Agent;
import it.polito.astrid.models.Configuration.Pipeline;
import it.polito.contextbroker.model.Execution_Environment;
import it.polito.contextbroker.model.Execution_Environment.LCP;
import it.polito.verefoo.astrid.jaxb.Components.Component;

public class InstanceAgents {
	private Component ContextBroker;
	private Configuration configuration;
	private static final Logger logger=LoggerFactory.getLogger(InstanceAgents.class);
	
	public InstanceAgents(Component ContextBroker, Configuration configuration){
		this.ContextBroker = ContextBroker;
		this.configuration=configuration;
	
	}
	
	public void createAllExec() throws IOException {
		logger.info("++++++++++ Deployement " );
		for (Pipeline pipelines : configuration.getDeployment().getPipelines()) {
			for (Agent agent : pipelines.getAgents()) {
				createExecutionEnvironment(agent);
			}
		}
		
	}
	

	private String createExecutionEnvironment(Agent agent) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		
		RestTemplate restTemplate = new RestTemplate();
		
		int uid = generateUniqueId();
		Execution_Environment exec = new Execution_Environment();
		Execution_Environment.LCP  lcp = exec.new LCP();
		lcp.setPort(agent.getPort());
		lcp.setHttps(false);
		exec.setLcp(lcp);
		exec.setDescription("polycube for ebpf "+uid);
		exec.setId("sc-ebpf-"+uid);
		exec.setType_id("container-docker");
		exec.setHostname( "node-0.astrid-kube");
		exec.setEnabled( "Yes");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(mapper.writeValueAsString(exec), headers);
		
		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/exec-env", requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ Execution Enviroment created with id = "+"sc-ebpf-"+uid);
			}else {
				logger.error("++++++++++ Execution Enviroment  with an error: " + result.getBody());
			}
		}catch(Exception e) {
			logger.error("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
		return "sc-ebpf-"+uid;
	}

	private int generateUniqueId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
