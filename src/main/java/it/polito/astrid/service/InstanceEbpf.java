package it.polito.astrid.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import it.polito.astrid.models.KafkaMessage;
import it.polito.contextbroker.model.Agent_Instance;
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
	
	private static final Logger logger=LoggerFactory.getLogger(InfrastructureInfoRequest.class);
	
	private Component ContextBroker;
	
	
	public InstanceEbpf(Component ContextBroker){
		this.ContextBroker = ContextBroker;
	
	}
	
	
	public void ebpfAlarm(String type, String inter) throws ContextBrokerException, IOException, JSONException, InterruptedException{
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
		//recovery the agent instance
		agentInstance = getAgentInstance();
		//recovery the execution environment
		String exec_id = createExecutionEnvironment();
		TimeUnit.SECONDS.sleep(50);
		creatDynMon(exec_id,type,inter);
	
	}
	
	 public static int generateUniqueId() {      
	        UUID idOne = UUID.randomUUID();
	        String str=""+idOne;        
	        int uid=str.hashCode();
	        String filterStr=""+uid;
	        str=filterStr.replaceAll("-", "");
	        return Integer.parseInt(str);
	    }
	
	private String createExecutionEnvironment() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		
		RestTemplate restTemplate = new RestTemplate();
		
		
		int uid = generateUniqueId();
		Execution_Environment exec = new Execution_Environment();
		Execution_Environment.LCP  lcp = exec.new LCP();
		lcp.setPort(5000);
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
	
	private  void creatDynMon(String ebpf_id, String type, String interface_d) throws IOException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "ASTRID " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOiIxNjIwMjQwNTEwIiwiaWF0IjoxNjIwMzI2NjIwLCJleHAiOjE2NTE4NjI2MjB9.qxhLtnwciHR0N-WANXh2Btw2zcPyDmjSxdkKJBXiy50");
		RestTemplate restTemplate = new RestTemplate();
		
		
		int uid = generateUniqueId();
		JSONObject dynObject = new JSONObject();
		dynObject.put("ebpf_program_catalog_id", type);
		dynObject.put("id", "dyn-id-"+uid);
		dynObject.put("description", "Collect");
		dynObject.put("exec_env_id", ebpf_id);
		dynObject.put("interface", interface_d);
		
		// Data attached to the request.
		HttpEntity<JSONObject> requestBody = new HttpEntity<JSONObject>( dynObject, headers);
		
		ResponseEntity<String> result = null;
		try {
			// Send request with POST method.
			result = restTemplate.postForEntity("http://" + ContextBroker.getIPAddress() + ":" + ContextBroker.getPort() + "/instance/ebpf-program", requestBody, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				System.out.println("++++++++++ DynMon created with id = "+"dyn-id-"+uid);
				System.out.println("++++++++++ "+result.getBody() );
			}else {
				System.out.println("++++++++++ DynMon  with an error: " + result.getBody());
			}
		}catch(Exception e) {
			System.out.println("++++++++++ error while contatcting Context Broker module: " + e.getMessage());
			System.out.println("++++++++++ error while contatcting Context Broker module: " + e.toString());
			throw new IOException();
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
