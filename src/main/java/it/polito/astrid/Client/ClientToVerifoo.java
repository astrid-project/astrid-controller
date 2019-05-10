package it.polito.astrid.Client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientToVerifoo {
	static final String URL_GRAPH= "localhost:8080/verifoo/rest/astrid/graph";
	static final String URL_INFO= "localhost:8080/verifoo/rest/astrid/info";
	static final String URL_DC= "localhost:8080/verifoo/rest/astrid/dc";
	 
 
    public ClientToVerifoo(String convertFileToString) {
    	 postGraphYaml(convertFileToString);
	}



	public static void main(String[] args) {
        simpleGet();
       
        
    }

    

	private static void postGraphYaml(String string) {
			 
		 
		        RestTemplate restTemplate = new RestTemplate();
		 
		        // Data attached to the request.
		        HttpEntity<String> requestBody = new HttpEntity<>(string);
		 
		        // Send request with POST method.
		        ResponseEntity<String> result 
		             = restTemplate.postForEntity(URL_GRAPH, requestBody, String.class);
		 
		        System.out.println("Status code:" + result.getStatusCode());
		 
		        // Code = 200.
		        if (result.getStatusCode() == HttpStatus.OK) {
		            String e = result.getBody();
		            System.out.println("(Client Side) Employee Created: "+ e);
		        }
		 
	}


	public static void simpleGet() {
		RestTemplate restTemplate = new RestTemplate();
        // Send request with GET method and default Headers.
        String result = restTemplate.getForObject(URL_INFO, String.class);
        System.out.println("Result of info:"+result);
	}
    
	
    
}
