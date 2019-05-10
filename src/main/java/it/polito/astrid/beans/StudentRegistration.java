package it.polito.astrid.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StudentRegistration {
	private List<Element> studentRecords;
	private static StudentRegistration stdregd = null;
	
	static final String URL_GRAPH= "http://localhost:8080/verifoo/rest/astrid/graph";
	static final String URL_INFO= "http://localhost:8080/verifoo/rest/astrid/info";
	

	private StudentRegistration() {
		studentRecords = new ArrayList<Element>();
	}

	public static StudentRegistration getInstance() {
		if (stdregd == null) {
			postGraphYaml(convertFileToString("src/main/resources/graph.yml")); 
			stdregd = new StudentRegistration();
			
			return stdregd;
		} else {
			return stdregd;
		}
	}
	
	 public static String convertFileToString(String string)  {
			
			String line;
			String fileAsString = null;
			try {
				FileInputStream file = new FileInputStream(string);
				BufferedReader buf = new BufferedReader(new InputStreamReader(file));
				line = buf.readLine();
				StringBuilder sb = new StringBuilder();
		        
				while(line != null){
				   sb.append(line).append("\n");
				   line = buf.readLine();
				}
				        
				 fileAsString = sb.toString();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		
			return fileAsString;
		}

	public void add(Element std) {
		studentRecords.add(std);
	}
	
	public void createClient(String std) {
	}
	
	public static String simpleGet() {
		RestTemplate restTemplate = new RestTemplate();
        // Send request with GET method and default Headers.
        String result = restTemplate.getForObject(URL_INFO, String.class);
        System.out.println("Result of info:"+result); 
        return result;
	}
    

	public String upDateStudent(Element std) {
		for (int i = 0; i < studentRecords.size(); i++) {
			Element stdn = studentRecords.get(i);
			if (stdn.getRegistrationNumber().equals(std.getRegistrationNumber())) {
				studentRecords.set(i, std);// update the new record
				return "Update successful";
			}
		}
		return "Update un-successful";
	}

	public String deleteStudent(String registrationNumber) {
		for (int i = 0; i < studentRecords.size(); i++) {
			Element stdn = studentRecords.get(i);
			if (stdn.getRegistrationNumber().equals(registrationNumber)) {
				studentRecords.remove(i);// update the new record
				return "Delete successful";
			}
		}
		return "Delete un-successful";
	}

	public List<Element> getStudentRecords() {
		return studentRecords;
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
}