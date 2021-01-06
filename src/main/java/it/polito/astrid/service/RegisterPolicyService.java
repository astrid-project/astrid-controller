package it.polito.astrid.service;

import java.io.IOException;
import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import it.polito.astrid.controllers.VerefooException;
import it.polito.verefoo.astrid.jaxb.Components.*;

public class RegisterPolicyService {
	
	private static final Logger logger=LoggerFactory.getLogger(RegisterPolicyService.class);
	
	public RegisterPolicyService() {
	}
	

	public ResponseEntity<String> registerPolicy(String policy, Component verefoo) throws VerefooException, IOException {
		logger.info("++++++++++ registerPolicy Controller Obtained Policy");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);

		RestTemplate restTemplate = new RestTemplate();

		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<>(policy, headers);

		logger.info("++++++++++ registerPolicy Controller Sending to Verekube");
		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + verefoo.getIPAddress() + ":" + verefoo.getPort() + "/verefoo/graph", requestBody, String.class);
			// Code = 200.
			logger.info("++++++++++ registerPolicy Controller Success from Verekube: " + result.getBody());
		}catch(HttpServerErrorException | HttpClientErrorException e) {
			logger.error("++++++++++ error while contacting Verefoo module: " + e.getMessage() + " - " + e.getLocalizedMessage());
			throw new VerefooException(e.getMessage());
		}catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		return result;
	}
}
