package it.polito.astrid.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import it.polito.astrid.controllers.ResourceNotFoundException;
import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.astrid.jaxb.Components.Component;

public class RegisterRemoveEventService {
	private static final Logger logger=LoggerFactory.getLogger(RegisterEventService.class);
	
	public RegisterRemoveEventService(){
	}
	
	public String registerEvent(@RequestBody InfrastructureEvent event, Component CB) throws ResourceNotFoundException, IOException, JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		logger.info("++++++++++ registerEvent Controller Obtained an Remove Event");
		
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		
		//costruct the query to remove the connection
		JSONObject query = new JSONObject();
		JSONObject where = new JSONObject();
		JSONObject equals = new JSONObject();
		equals.put("target", "exec_env_id");
		equals.put("expr", event.getEventData().getUid());
		where.put("equals", equals);
		query.put("where", where);
		
		HttpEntity<String> requestBody = new HttpEntity<String>(query.toString(), headers);
		logger.info("++++++++++ registerEvent Controller Sending Event info to Context Broker");

		try {
			restTemplate.delete("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/connection", requestBody, String.class);
			logger.info("++++++++++ successful disconnect the instance. Deleting the instance from Context Broker in progress...");
			equals.remove("target");
			equals.put("target", "id");
			requestBody = new HttpEntity<String>(query.toString(), headers);
			restTemplate.delete("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/exec-env", requestBody, String.class);
			logger.info("++++++++++ successful delete the instance");
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			logger.error("++++++++++ registerEvent Controller error. " + ex.getResponseBodyAsString());
			throw new ResourceNotFoundException("Error: "+ex.getResponseBodyAsString() );
		} catch (Exception e) {
			logger.error("++++++++++ error while contacting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
		return event.getGraphName();
	}
}
