package it.polito.astrid.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import it.polito.astrid.controllers.*;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.jaxb.NFV;
import it.polito.verefoo.astrid.jaxb.Components.*;

public class RegisterInfrastructureService {
	
	private static final Logger logger=LoggerFactory.getLogger(RegisterInfrastructureService.class);
	
	public RegisterInfrastructureService() {
	}
	
	public ResponseEntity<NFV> registerInfrastructure(InfrastructureInfo info, Component verefoo) throws ResourceNotFoundException, IOException {
		
		if (info == null || info.getMetadata() == null || info.getMetadata().getName() == null) {
			logger.info("++++++++++ registerInfrastructure Empty body");
			throw new ResourceNotFoundException("registerInfrastructure Empty body");
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);

		logger.info("++++++++++ registerInfrastructure Controller Obtained Infrastructure info for: "
				+ info.getMetadata().getName());
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		// Data attached to the request.
		HttpEntity<InfrastructureInfo> requestBody = new HttpEntity<InfrastructureInfo>(info, headers);

		logger.info("++++++++++ registerInfrastructure Controller Sending Infrastructure info to Verekube");
		// Send request with POST method.
		ResponseEntity<NFV> result=null;
		try {
			 result = restTemplate.postForEntity("http://" + verefoo.getIPAddress() + ":" + verefoo.getPort() + "/verefoo/dc", requestBody, NFV.class);
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			logger.info("++++++++++ registerInfrastructure Controller  Policy is not defined for this graph");
			throw new ResourceNotFoundException("Policy is not defined for this graph: "+ex.getMessage() );
		} catch (Exception e) {
			logger.info("++++++++++ error while contatcting Verefoo module: " + e.getMessage());
			throw new IOException();
		}
	
		return result;
	}
}
