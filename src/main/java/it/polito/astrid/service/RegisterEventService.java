package it.polito.astrid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.Components.*;

public class RegisterEventService {
	
	private static final Logger logger=LoggerFactory.getLogger(RegisterEventService.class);
	private Neo4jService neo4j = null;
	
	public RegisterEventService() {
	}
	
	public String registerEvent(InfrastructureEvent event, Component astrid_db){
		logger.info("++++++++++ registerEvent Controller Obtained Event of type: " + event.getType());
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
	
}
