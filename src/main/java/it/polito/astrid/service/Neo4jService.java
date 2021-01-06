package it.polito.astrid.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4jService implements AutoCloseable {
	
	private static final Logger logger=LoggerFactory.getLogger(Neo4jService.class);
	private Driver driver;
	
	public Neo4jService(String url, String port, String username, String password) {
		try {
			driver = GraphDatabase.driver("bolt://" + url + ":" + port, AuthTokens.basic(username, password));
		}catch(Exception e) {
			driver = null;
		}
	}
	
	public boolean writing(String EventType, String resourceType, String Name, String ip, String uid) {
		try {
			Session session = driver.session();
			session.writeTransaction(tx -> tx.run("CREATE (:InfrastructureEvent {Type: '" + EventType + "'}) - [:Event] -> (:EventData {resourceType: '" + resourceType + 
					"', name: '" + Name + "', ip: '" + ip + "', uid: '" + uid +"'})"));
		}catch (Exception e) {
			logger.debug(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
	
}
