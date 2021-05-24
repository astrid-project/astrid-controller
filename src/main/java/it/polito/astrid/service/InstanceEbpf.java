package it.polito.astrid.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import it.polito.astrid.models.KafkaMessage;
import it.polito.contextbroker.model.Agent_Instance;
import it.polito.contextbroker.model.Execution_Environment;
import it.polito.contextbroker.model.actions;
import it.polito.verefoo.astrid.jaxb.Components.Component;
import it.polito.verefoo.jaxb.Elements;
import it.polito.verefoo.jaxb.Graph;
import it.polito.verefoo.jaxb.NFV;
import it.polito.verefoo.jaxb.Node;

public class InstanceEbpf {
	
	private static final Logger logger=LoggerFactory.getLogger(InfrastructureInfoRequest.class);
	
	private Component ContextBroker;
	
	
	public InstanceEbpf(Component ContextBroker){
		this.ContextBroker = ContextBroker;
	
	}
	
	
	public void instanceFirewallFromKafkaEvent(KafkaMessage kmes) throws ContextBrokerException{
		logger.info("+++++++++ DDoS LOIC attack detected!");
		List<Agent_Instance> agentInstance = new ArrayList<>();
		List<Execution_Environment> exec_env = new ArrayList<>();
		int i = 0;
		Date dateNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestBody = null;
		ResponseEntity<String> result = null;
		JSONObject query = null;
		JSONObject defAction = null;
		JSONObject rule = null;
		JSONArray actions = null;
		List<JSONObject> rules = new ArrayList<>();
		boolean trovato = false;
		//recovery the agent instance
		agentInstance = getAgentInstance();
		//recovery the execution environment
		exec_env = getExecutionEnvironment();
		Iterator<Execution_Environment> it = exec_env.iterator();
		
		//recovery the id of exec_env that has the IP egual to source IP of the kafka message
		while(it.hasNext() && trovato==false) {
			Execution_Environment node = it.next();
			if((node.getHostname().compareTo(kmes.getSource_ip())==0) || (node.getHostname().compareTo(kmes.getDestination_ip())==0)) {
				trovato = true;
				query = new JSONObject();
				defAction = new JSONObject();
				actions = new JSONArray();
				//check if already exist a firewall of that node
				if (checkFirewallExist(node.getId(), agentInstance)==true) {
					//firewall already exist, so update the rules
					Agent_Instance fw = getFirewallwithExecEnvId(node.getId(), agentInstance);
					try {
						query.put("id", "firewall@" + node.getId());
						//retrieve the action list
						List<actions> act = fw.getActions();
						Iterator<actions> act_it = act.iterator();
						i=0;
						while(act_it.hasNext()) {
							actions action = act_it.next();
							rule = new JSONObject();
							try {
								rule.put("id", action.getId());
								if (action.getN() != null)
									rule.put("n", action.getN());
								if(action.getSrc() != null)
									rule.put("src", action.getSrc());
								if(action.getDst() != null)
									rule.put("dst", action.getDst());
								if(action.getAction() != null)
									rule.put("action", action.getAction());
								if(action.getTimestamp() != null)
									rule.put("timestamp", action.getTimestamp());
								rules.add(rule);
								actions.put(rules.get(i));
								i++;
							}catch(Exception ex) {
								logger.error("+++++++++ error while costruct the query: " + ex.getMessage());
								throw new ContextBrokerException(ex.getMessage());
							}
						}
						rule = new JSONObject();
						if(node.getHostname().compareTo(kmes.getSource_ip())==0) {
							//the node is the sender of the attack
							rule.put("id", "insert");
							rule.put("n", "rule" + i + "@" + node.getId());
							rule.put("src", node.getHostname());
							rule.put("dst", kmes.getDestination_port());
							rule.put("action", "DENY");
							rule.put("timestamp", ft.format(dateNow));
						}else {
							//the node is the target of the attack
							rule.put("id", "insert");
							rule.put("n", "rule" + i + "@" + node.getId());
							rule.put("src", kmes.getSource_ip());
							rule.put("dst", node.getHostname());
							rule.put("action", "DENY");
							rule.put("timestamp", ft.format(dateNow));
						}
						rules.add(rule);
						actions.put(rules.get(i));
						i++;
						query.put("actions", actions);
					}catch(Exception ex) {
						logger.error("+++++++++ error while costruct query: " + ex.getMessage());
						throw new ContextBrokerException(ex.getMessage());
					}
					//send the request
					requestBody = new HttpEntity<String>(query.toString(), headers);
					try {
						restTemplate.put("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent", requestBody);
						logger.info("+++++++++ Firewall to block attack successfully updated!");
					}catch (HttpClientErrorException e) {
						if(e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
							logger.info("+++++++++ Unable to reach the firewall, but insert it into Context Broker");
						}else {
							logger.error("+++++++++ error while instance the firewalls to block attack: " + e.getMessage());
							throw new ContextBrokerException(e.getMessage());
						}
					}catch (HttpServerErrorException e) {
						logger.error("+++++++++ error while instance the firewalls to block attack: " + e.getMessage());
						throw new ContextBrokerException(e.getMessage());
					}
					
				}else {
					//firewall doesn't exist, so create it
					try {
						query.put("id", "firewall@" + node.getId());
						query.put("agent_catalog_id", "firewall");
						query.put("exec_env_id", node.getId());
						query.put("status", "started");
						defAction.put("id", "default");
						defAction.put("action", "DENY");
						defAction.put("timestamp", ft.format(dateNow));
						actions.put(defAction);
						query.put("actions", actions);
					}catch(Exception ex) {
						logger.error("+++++++++ error while costruct the query: " + ex.getMessage());
						throw new ContextBrokerException(ex.getMessage());
					}
					//send the request
					requestBody = new HttpEntity<String>(query.toString(), headers);
					try {
						result = restTemplate.postForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent", requestBody, String.class);
						logger.info("+++++++++ Firewall to block attack successfully instanced!");
					}catch (HttpClientErrorException e) {
						if(e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
							logger.info("+++++++++ Unable to reach the firewall, but insert it into Context Broker");
						}else {
							logger.error("+++++++++ error while instance the firewall to block attack: " + e.getMessage());
							throw new ContextBrokerException(e.getMessage());
						}
					}catch (HttpServerErrorException e) {
						logger.error("+++++++++ error while instance the firewall to block attack: " + e.getMessage());
						throw new ContextBrokerException(e.getMessage());
					}
				}
			}
		}
	}
	
	private String getAddressWithMask(String ip){
		String[] parti = ip.split(Pattern.quote("."));
		if(parti[3].compareTo("-1")==0) {
			return (parti[0] + "." + parti[1] + "." + parti[2] + ".0/24");
		}else if (parti[2].compareTo("-1")==0) {
			return (parti[0] + "." + parti[1] + ".0.0/16");
		}else if(parti[1].compareTo("-1")==0) {
			return (parti[0] + ".0.0.0/8");
		}
		return ip;
	}
	
	private List<Execution_Environment> getExecutionEnvironment() throws ContextBrokerException {
		List<Execution_Environment> exec_env = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		try {
			ResponseEntity<String> ee = restTemplate.getForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/exec-env", String.class);
			exec_env = Arrays.asList(objectMapper.readValue(ee.getBody(), Execution_Environment[].class));
		}catch (Exception e) {
			logger.error("+++++++++ error while asking the execution environments to Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		}
		return exec_env;
	}
	
	private List<Agent_Instance> getAgentInstance() throws ContextBrokerException {
		List<Agent_Instance> agentInstance = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		ResponseEntity<String> result = null;
		try {
			result = restTemplate.getForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent", String.class);
			agentInstance = objectMapper.readValue(result.getBody(), new TypeReference<List<Agent_Instance>>(){});
		}catch(HttpServerErrorException ex) {
			logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + ex.getMessage());
			throw new ContextBrokerException(ex.getStatusCode().toString());
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() != HttpStatus.NOT_FOUND) {
				logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + e.getMessage());
				throw new ContextBrokerException(e.getMessage());
			}
		} catch (JsonParseException e) {
			logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		} catch (IOException e) {
			logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + e.getMessage());
			throw new ContextBrokerException(e.getMessage());
		}
		return agentInstance;
	}
	
	private boolean checkFirewallExist(String exec_env_id, List<Agent_Instance> ai) {
		Iterator<Agent_Instance> it = ai.iterator();
		while(it.hasNext()) {
			Agent_Instance node = it.next();
			if((node.getAgent_catalog_id().compareTo("firewall") == 0) && (node.getExec_env_id().compareTo(exec_env_id) == 0)) {
				return true;
			}
		}
		return false;
	}
	
	private Agent_Instance getFirewallwithExecEnvId(String exec_env_id, List<Agent_Instance> ai) throws ContextBrokerException {
		Iterator<Agent_Instance> it = ai.iterator();
		while(it.hasNext()) {
			Agent_Instance node = it.next();
			if((node.getAgent_catalog_id().compareTo("firewall") == 0) && (node.getExec_env_id().compareTo(exec_env_id) == 0)) {
				return node;
			}
		}
		throw new ContextBrokerException("Unable to find a Firewall with specified ID");
	}
}
