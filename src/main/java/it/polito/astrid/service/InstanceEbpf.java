package it.polito.astrid.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.json.JSONArray;
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.astrid.controllers.ContextBrokerException;
import it.polito.astrid.models.DynMonIDs;
import it.polito.astrid.models.KafkaMessage;
import it.polito.contextbroker.model.Agent_Instance;
import it.polito.contextbroker.model.Ebpf;
import it.polito.contextbroker.model.Execution_Environment;
import it.polito.contextbroker.model.Execution_Environment.LCP;
import it.polito.contextbroker.model.actions;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.astrid.jaxb.Components.Component;
import it.polito.verefoo.jaxb.Elements;
import it.polito.verefoo.jaxb.Graph;
import it.polito.verefoo.jaxb.NFV;
import it.polito.verefoo.jaxb.Node;

public class InstanceEbpf {

	private static final Logger logger = LoggerFactory.getLogger(InstanceEbpf.class);

	private Component ContextBroker;
	Map<String, String> dynMap;
	public InstanceEbpf(Component ContextBroker, DynMonIDs ids) {
		this.dynMap = ids.setDID;
		this.ContextBroker = ContextBroker;
	
	}

	public void ebpfAlarmRm(String command, String type, String message)
			throws ContextBrokerException, IOException, JSONException, InterruptedException {
		List<Ebpf> ebpfs = getEbpfCodes();
		logger.info("TTTTTTTTTTTTTTTTT Found ebpfs before remove	 "+ebpfs.size());
		for (Ebpf ebpf : ebpfs) {
			
			if (ebpf.getEbpf_program_catalog_id().equals(command)) {
				String catalogId =dynMap.get(ebpf.getId());
				logger.info("++++++++++ Searching in map for "+catalogId+" of ID "+ebpf.getId()+ " for message "+message);
				if(catalogId!=null)
				if(!catalogId.equals(message)) {
					logger.info("$$$$$$$ Found Ebpf to remove " + ebpf.getId()+" message "+dynMap.get(ebpf.getId()));
					remDynMon(ebpf.getId());
				}
				
				
				//remDynMon(ebpf.getId());

			}
		}
	}

	private List<Ebpf> getEbpfCodes() {
		List<Ebpf> exec_env = new ArrayList<>();
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
			ResponseEntity<String> response = restTemplate.exchange(
					"http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/ebpf-program",
					HttpMethod.GET, requestBody, String.class, 1);
			exec_env = Arrays.asList(objectMapper.readValue(response.getBody(), Ebpf[].class));
		} catch (Exception e) {
		}
		return exec_env;
	}

	private boolean remDynMon(String id) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID "
				+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		RestTemplate restTemplate = new RestTemplate();
		int uid = generateUniqueId();
		// Data attached to the request.
		HttpEntity<?> requestBody = new HttpEntity<Object>(headers);
		ResponseEntity<String> result = null;

		try {
			// Send request with POST method.
			String urlA = "http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort()
					+ "/instance/ebpf-program/" + id;
			result = restTemplate.exchange(urlA, HttpMethod.DELETE, requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ Execution Enviroment created with id = " + "sc-ebpf-" + uid);
				return true;
			} else {
				logger.error("++++++++++ Execution Enviroment  with an error: " + result.getBody());
				return false;
			}
		} catch (Exception e) {
			logger.error("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
	}

	public String ebpfAlarm(String type, String inter, String message)
			throws ContextBrokerException, IOException, JSONException, InterruptedException {
		logger.info("+++++++++ EBPF deploying " + type + " interface " + inter);
		List<Execution_Environment> exec_env = new ArrayList<>();
		exec_env = getExecutionEnvironment();
		logger.info("+++++++++ Exec-envs  = "+exec_env.size());
		Iterator<Execution_Environment> it = exec_env.iterator();
		boolean trovato = false;
		// recovery the id of exec_env that has the IP egual to source IP of the kafka
		// message
		while (it.hasNext() && trovato == false) {
			Execution_Environment node = it.next();
			if(node.getId().startsWith("sc-ebpf")) {
				logger.info("+++++++++ Found exec-env "+node.getId());
				creatDynMon(node.getId(), type, inter,message);
			}
				
			
		}

		return "";

	}

	public static int generateUniqueId() {
		UUID idOne = UUID.randomUUID();
		String str = "" + idOne;
		int uid = str.hashCode();
		String filterStr = "" + uid;
		str = filterStr.replaceAll("-", "");
		return Integer.parseInt(str);
	}

	private String creatDynMon(String ebpf_id, String type, String interface_d, String message) throws IOException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID "
				+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		RestTemplate restTemplate = new RestTemplate();

		int uid = generateUniqueId();
		JSONObject dynObject = new JSONObject();
		dynObject.put("ebpf_program_catalog_id", type);
		dynObject.put("id", "dyn-id-" + uid);
		dynObject.put("description", "Collect");
		dynObject.put("exec_env_id", ebpf_id);
		dynObject.put("interface", interface_d);

		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(dynObject.toString(), headers);

		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity(
					"http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/ebpf-program",
					requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("++++++++++ DynMon created with id = " + "dyn-id-" + uid);
				logger.info("++++++++++ " + result.getBody());
			
			} else {
				logger.info("++++++++++ DynMon  with an error: " + result.getBody());
			}
		} catch (Exception e) {
			logger.info("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			logger.info("++++++++++ error while contatcting Context Broker module: " + e.toString());
			throw new IOException();
		}
		dynMap.put("dyn-id-"+uid,message);
		logger.info("--------- inserting to map key= " +"dyn-id-"+uid+ " value "+message	);
		return "dyn-id-" + uid;
	}

	private String getAddressWithMask(String ip) {
		String[] parti = ip.split(Pattern.quote("."));
		if (parti[3].compareTo("-1") == 0) {
			return (parti[0] + "." + parti[1] + "." + parti[2] + ".0/24");
		} else if (parti[2].compareTo("-1") == 0) {
			return (parti[0] + "." + parti[1] + ".0.0/16");
		} else if (parti[1].compareTo("-1") == 0) {
			return (parti[0] + ".0.0.0/8");
		}
		return ip;
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
			ResponseEntity<String> response = restTemplate.exchange(
					"http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/exec-env",
					HttpMethod.GET, requestBody, String.class, 1);
			exec_env = Arrays.asList(objectMapper.readValue(response.getBody(), Execution_Environment[].class));
		} catch (Exception e) {
			logger.error(
					"+++++++++ error while asking the execution environments to Context Broker: " + e.getMessage());
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
			result = restTemplate.getForEntity(
					"http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/agent",
					String.class);
			agentInstance = objectMapper.readValue(result.getBody(), new TypeReference<List<Agent_Instance>>() {
			});
		} catch (HttpServerErrorException ex) {
			logger.error("+++++++++ error while recover the Agent instance from Context Broker: " + ex.getMessage());
			throw new ContextBrokerException(ex.getStatusCode().toString());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
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
		while (it.hasNext()) {
			Agent_Instance node = it.next();
			if ((node.getAgent_catalog_id().compareTo("firewall") == 0)
					&& (node.getExec_env_id().compareTo(exec_env_id) == 0)) {
				return true;
			}
		}
		return false;
	}

	private Agent_Instance getFirewallwithExecEnvId(String exec_env_id, List<Agent_Instance> ai)
			throws ContextBrokerException {
		Iterator<Agent_Instance> it = ai.iterator();
		while (it.hasNext()) {
			Agent_Instance node = it.next();
			if ((node.getAgent_catalog_id().compareTo("firewall") == 0)
					&& (node.getExec_env_id().compareTo(exec_env_id) == 0)) {
				return node;
			}
		}
		throw new ContextBrokerException("Unable to find a Firewall with specified ID");
	}
}
