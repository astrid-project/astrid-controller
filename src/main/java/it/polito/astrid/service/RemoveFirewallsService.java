package it.polito.astrid.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.astrid.controllers.ContextBrokerException;
import it.polito.contextbroker.model.Agent_Instance;
import it.polito.contextbroker.model.Connection;
import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.Components.Component;
import it.polito.verefoo.jaxb.Graph;
import it.polito.verefoo.jaxb.NFV;
import it.polito.verefoo.jaxb.Node;

public class RemoveFirewallsService {
	
	private static final Logger logger=LoggerFactory.getLogger(RemoveFirewallsService.class);
	
	private Component ContextBroker;
	
	public RemoveFirewallsService(Component ContextBroker){
		this.ContextBroker = ContextBroker;
	}
	
	public void removeFirewallfromInfrastructureEvent(InfrastructureEvent event) throws ContextBrokerException {
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		try {
			restTemplate.delete("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent/firewall@" + event.getEventData().getUid());
			logger.info("+++++++++ Firewall with ID: firewall@" + event.getEventData().getUid() + " successfully deleted!");
		}catch(Exception e) {
			logger.error("+++++++++ error while deleting the firewall with id: firewall@" +  event.getEventData().getUid() + " - " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		}
	}
	
	public void RemoveAllFirewallfromGraph(String graph) throws ContextBrokerException, InterruptedException {
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		//recover the connections
		List<Connection> conn = getConnections();
		Iterator<Connection> it = conn.iterator();
		
		while(it.hasNext()) {
			Connection c = it.next();
			if(c.getNetwork_link_id().compareTo(graph)==0) {
				try {
					restTemplate.delete("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent/firewall@" + c.getExec_env_id());
					logger.info("+++++++++ Firewall with ID: firewall@" + c.getExec_env_id() + " successfully deleted!");
				}catch(Exception e) {
					logger.error("+++++++++ error while deleting the firewall with id: firewall@" +  c.getExec_env_id() + " - " + e.getMessage());
					throw new ContextBrokerException(e.getMessage());
				}
			}
		}
		TimeUnit.SECONDS.sleep(1);
	}
	
	private List<Connection> getConnections() throws ContextBrokerException{
		List<Connection> conn = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		ResponseEntity<String> result = null;
		try {
			result = restTemplate.getForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/connection", String.class);
			conn = objectMapper.readValue(result.getBody(), new TypeReference<List<Connection>>(){});
		}catch(HttpServerErrorException ex) {
			logger.error("+++++++++ error while recover the connections from Context Broker: " + ex.getMessage());
			throw new ContextBrokerException(ex.getStatusCode().toString());
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() != HttpStatus.NOT_FOUND) {
				logger.error("+++++++++ error while recover the connections from Context Broker: " + e.getMessage());
				throw new ContextBrokerException(e.getMessage());
			}
		} catch (JsonParseException e) {
			logger.error("+++++++++ error while recover the connections from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("+++++++++ error while recover the connections from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		} catch (IOException e) {
			logger.error("+++++++++ error while recover the connections from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		}
		return conn;
	}
	
	public void removeOldVersionOfFirewallsFromNFV(NFV graph) throws ContextBrokerException, InterruptedException{
		//recover the node that are firewalls from NFV
		Iterator<Graph> it = graph.getGraphs().getGraph().iterator();
		Graph schema = it.next();
		Iterator<Node> it_node = schema.getNode().iterator();
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		
		
		ResponseEntity<String> result = null;
		while(it_node.hasNext()) {
			Node n = it_node.next();
			if(n.getFunctionalType().toString().compareTo("FIREWALL") == 0) {
				//check if firewall already exist
				//check if a firewall already exist of this node
				logger.info("+++++++++ check if firewall with ID: firewall@" + n.getConfiguration().getName() + " exist");
				try {
					result = restTemplate.getForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent/firewall@" + n.getConfiguration().getName(), String.class);
					logger.info("+++++++++ A previous version of firewall with ID: firewall@" + n.getConfiguration().getName() + " exist! Deleting in progress...");
					try {
						restTemplate.delete("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent/firewall@" + n.getConfiguration().getName());
						logger.info("+++++++++ Firewall with ID: firewall@" + n.getConfiguration().getName() + " successfully deleted!");
					}catch(Exception e) {
						logger.error("+++++++++ error while deleting the firewall with id: firewall@" +  n.getConfiguration().getName() + " - " + e.getMessage());
						throw new ContextBrokerException(e.getMessage());
					}
				}catch (HttpClientErrorException e) {
					if(e.getStatusCode() != HttpStatus.NOT_FOUND) {
						logger.error("+++++++++ error while checking if firewall with id: firewall@" +  n.getConfiguration().getName() + " exist. " + e.getMessage());
						throw new ContextBrokerException(e.getMessage());
					}else {
						logger.info("+++++++++ There isn't any firewall with ID: firewall@" + n.getConfiguration().getName());
					}
				}catch(HttpServerErrorException e) {
					logger.error("+++++++++ error while checking if firewall with id: firewall@" +  n.getConfiguration().getName() + " exist. " + e.getMessage());
					throw new ContextBrokerException(e.getMessage());
				}
				TimeUnit.SECONDS.sleep(1);
			}
		}
		
		
	}
}
