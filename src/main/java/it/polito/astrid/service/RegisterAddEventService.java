package it.polito.astrid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.Components.Component;

public class RegisterAddEventService {
	
	private static final Logger logger=LoggerFactory.getLogger(RegisterEventService.class);
	private Neo4jService neo4j = null;
	
	public RegisterAddEventService(){
	}
	
	public String registerEvent(InfrastructureEvent event, Component astrid_db){
		logger.info("++++++++++ registerEvent Controller Obtained an Add Event");
		if(astrid_db.getIPAddress().compareTo("0.0.0.0")==0) {
			logger.info("++++++++++ There isn't any Astrid DB configured!");
		}else {
			neo4j = new Neo4jService(astrid_db.getIPAddress(), astrid_db.getPort().toString(), astrid_db.getUsername(), astrid_db.getPassword());
			boolean n = neo4j.writing(event.getType(), event.getEventData().getResourceType(), event.getEventData().getName(), event.getEventData().getIp(), event.getEventData().getUid());
			if (n==true)
				logger.info("++++++++++ Event correctly insert into Astrid DB");
			else
				logger.error("++++++++++ Error while insert the event into Astrid DB");
		}
		return event.getGraphName();
	}
	
	
	/*public String registerEvent(InfrastructureEvent event, Component CB) throws ResourceNotFoundException, IOException, JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logger.info("++++++++++ registerEvent Controller Obtained an Add Event");
		
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		
		//costruct the query to insert the new instance into CB
		JSONObject queryconn = new JSONObject();
		JSONObject lcp = new JSONObject();
		JSONObject query = new JSONObject();
		
		query.put("id", event.getEventData().getUid());
		query.put("description", event.getEventData().getName() + " instance");
		query.put("type_id", event.getEventData().getName());
		query.put("hostname", event.getEventData().getIp());
		query.put("enabled", "true");
		lcp.put("port", getPortNumber(CB, event.getEventData().getName()).toString());
		query.put("lcp", lcp);
		
		HttpEntity<String> requestBody = new HttpEntity<String>(query.toString(), headers);
		logger.info("++++++++++ registerEvent Controller Sending Event info to Context Broker");
		
		try {
			restTemplate.postForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/exec-env", requestBody, String.class);
			TimeUnit.SECONDS.sleep(1);
			logger.info("++++++++++ successful create new instance into Context Broker. Connect of this new instance in progress...");
			//insert this new instance into connection
			queryconn.put("id", event.getEventData().getUid() + "_connection");
			queryconn.put("exec_env_id", event.getEventData().getUid());
			queryconn.put("network_link_id", event.getGraphName());
			queryconn.put("description", event.getEventData().getUid() + " network connection");
			requestBody = new HttpEntity<String>(queryconn.toString(), headers);
			restTemplate.postForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/connection", requestBody, String.class);
			TimeUnit.SECONDS.sleep(1);
			logger.info("++++++++++ successful connect of this new instance");
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			logger.error("++++++++++ registerEvent Controller error. " + ex.getResponseBodyAsString());
			throw new ResourceNotFoundException("Error: "+ex.getResponseBodyAsString() );
		} catch (Exception e) {
			logger.error("++++++++++ error while contacting Context Broker module: " + e.getMessage());
			throw new IOException();
		}
		return event.getGraphName();
	}*/
	
	/*private Integer getPortNumber(Component CB, String name) throws JsonParseException, JsonMappingException, IOException {
		List<Execution_Environment> exec_env = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter, new StringHttpMessageConverter()));
		ResponseEntity<String> ee = restTemplate.getForEntity("http://" + CB.getIPAddress() + ":" + CB.getPort() + "/exec-env", String.class);
		exec_env = Arrays.asList(objectMapper.readValue(ee.getBody(), Execution_Environment[].class));
		Iterator<Execution_Environment> it = exec_env.iterator();
		while(it.hasNext()) {
			Execution_Environment node = it.next();
			if(node.getType_id().compareTo(name)==0) {
				return node.getLcp().getPort();
			}
		}
		return 0;
	}*/
}
