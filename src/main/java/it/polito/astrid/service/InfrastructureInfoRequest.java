package it.polito.astrid.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.astrid.controllers.AstridComponentNotFoundException;
import it.polito.astrid.controllers.ContextBrokerException;
import it.polito.astrid.controllers.ResourceNotFoundException;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.*;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Spec.*;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Spec.Service.Instance;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Spec.Service.Port;
import it.polito.verefoo.astrid.jaxb.Components.*;
import it.polito.contextbroker.model.*;

public class InfrastructureInfoRequest {
	
	private static final Logger logger=LoggerFactory.getLogger(InfrastructureInfoRequest.class);
	
	private ResponseEntity<String> resultPolicy;
	private Component ContextBroker;
	
	public InfrastructureInfoRequest(ResponseEntity<String> res, Component cb) {
		this.resultPolicy = res;
		this.ContextBroker = cb;
	}
	
	public InfrastructureInfoRequest(Component cb) {
		this.ContextBroker = cb;
	}
	
	public InfrastructureInfo getInfrastructureInfo() throws AstridComponentNotFoundException, IOException, ResourceNotFoundException, JSONException, ContextBrokerException {
		InfrastructureInfo network = null;
		if(resultPolicy.getStatusCode() == HttpStatus.OK) {
			String name = ((resultPolicy.getBody()).substring(resultPolicy.getBody().indexOf(":")+1)).trim();
			logger.info("+++++++++ ask to Context Broker the infrastructure info of graph: " + name);
			network = getInfrastructureInfoFromCB(name, ContextBroker);
		}
		return network;
	}
	
	public InfrastructureInfo getInfrastructureInfo(String graphName) throws AstridComponentNotFoundException, IOException, ResourceNotFoundException, JSONException, ContextBrokerException {
		InfrastructureInfo network = null;
		logger.info("+++++++++ ask to Context Broker the infrastructure info of graph: " + graphName);
		network = getInfrastructureInfoFromCB(graphName, ContextBroker);
		return network;
	}
	
	private ResponseEntity<InfrastructureInfo> getInfrastructureInfoFromSimulatedCB(String graphName, Component CB) throws AstridComponentNotFoundException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(graphName, headers);
		
		ResponseEntity<InfrastructureInfo> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/getinfrastructure", requestBody, InfrastructureInfo.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ Infrastructure Info success receive from Context Broker");
			}else {
				logger.error("++++++++++ Context Broker answer with an error: " + result.getBody());
			}
		}catch(Exception e) {
			logger.error("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
		return result;
	}
	
	private InfrastructureInfo getInfrastructureInfoFromCB(String graphName, Component CB) throws AstridComponentNotFoundException, IOException, JSONException, ContextBrokerException {
		InfrastructureInfo net = new InfrastructureInfo();
		ObjectMapper objectMapper = new ObjectMapper();
		int i=0;
		
		//create request to get the connection into the network
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		// Data attached to the request.
		
		ResponseEntity<String> result = null;
		try {
			// Send request with GET method
			result = restTemplate.getForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/connection", String.class);
			logger.info("++++++++++ Context Broker answer successfully with connection of requested network");
		}catch(HttpServerErrorException e) {
			logger.error("++++++++++ error while contacting Context Broker module: " + e.getMessage());
			throw new IOException();
		}catch (HttpClientErrorException e) {
			logger.error("++++++++++ Context Broker answer with an error: " + e.getMessage());
			throw new ContextBrokerException("Context Broker answer with an error: " + e.getMessage());
		}
		//create a list with connections received
		List<Connection> connection = objectMapper.readValue(result.getBody(), new TypeReference<List<Connection>>(){});
			
		List<Execution_Environment> exec_env = new ArrayList<>();
		ResponseEntity<String> ee = null;
		//ask to CB to get the exec_env details with that ID
		for (i=0;i<connection.size();i++){
			if(connection.get(i).getNetwork_link_id().compareTo(graphName)==0){
				try {
					ee = restTemplate.getForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/exec-env/" + connection.get(i).getExec_env_id(), String.class);
					exec_env.add(objectMapper.readValue(ee.getBody().substring(1, ee.getBody().length()-1), Execution_Environment.class));
				} catch(HttpServerErrorException e) {
					logger.error("++++++++++ error while contacting Context Broker module: " + e.getMessage());
					throw new IOException();
				}catch(HttpClientErrorException e) {
					logger.error("++++++++++ Context Broker answer with an error while obtain exec-env: " + e.getMessage());
					throw new ContextBrokerException("Context Broker answer with an error while obtain exec-env: " + e.getMessage());
				}
			}
		}
			
		//now we can create the infrastructureInfo to send as answer
		Metadata meta = new Metadata();
		meta.setName(graphName);
		net.setMetadata(meta);
			
		Spec spec = new Spec();
		Iterator<Service> it = spec.getService().iterator();
		i=0;
		Service s;
		Port p;
		Instance ins;
		boolean found = false;
		for(i=0;i<exec_env.size();i++){
			found = false;
			it = spec.getService().iterator();
			while(it.hasNext() && found == false) {
				Service ser = it.next();
				if(ser.getName().equals(exec_env.get(i).getType_id())){
					//this is a new instance of an existing service
					found = true;
					ins = new Instance();
					ins.setIp(exec_env.get(i).getHostname());
					ins.setUid(exec_env.get(i).getId());
					ser.getInstance().add(ins);
				}}
				if(found == false) {
					//this is a new service
					s = new Service();
					p = new Port();
					ins = new Instance();
					s.setName(exec_env.get(i).getType_id());
					p.setInternal(exec_env.get(i).getLcp().getPort().shortValue());
					s.setPort(p);
					ins.setIp(exec_env.get(i).getHostname());
					ins.setUid(exec_env.get(i).getId());
					s.getInstance().add(ins);
					spec.getService().add(s);
				}
		}
		net.setSpec(spec);
		return net;
	}
	
	private String convertToXML(InfrastructureInfo event) {
		Marshaller marshaller=null;
		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();
		try {
			jaxbContext = JAXBContext.newInstance(InfrastructureInfo.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(event, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
