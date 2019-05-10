package it.polito.astrid.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import it.polito.astrid.Client.ClientToVerifoo;
import it.polito.astrid.beans.Element;
import it.polito.astrid.beans.StudentRegistration;
import it.polito.astrid.jaxb.InfrastructureInfo;
import it.polito.astrid.beans.RegistrationReply;

@Controller
public class RegistrationController {
	static final String URL_DC= "http://localhost:8080/verifoo/rest/astrid/dc";

	
	
	@RequestMapping(method = RequestMethod.POST, value = "/register/insfrastructure")
	@ResponseBody
	public String registerStudent(@RequestBody String info) {
		StudentRegistration.getInstance().simpleGet();
		System.out.println("In registerStudent");
		RestTemplate restTemplate = new RestTemplate();
		
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<>(info);

		// Send request with POST method.
		ResponseEntity<String> result = restTemplate.postForEntity(URL_DC, requestBody, String.class);
		System.out.println("Status code:" + result.getStatusCode());
		// Code = 200.
		if (result.getStatusCode() == HttpStatus.OK) {
			System.out.println("(Client Side) Employee Created: " + (String) (result.getBody()));
		}
		return result.getBody();
	}

}