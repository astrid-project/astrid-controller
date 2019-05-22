package it.polito.astrid.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.jaxb.NFV;


//mvn clean package && java -jar target\controller-0.0.1-SNAPSHOT.jar

@Controller
public class RegistrationController {
	static final String URL_DC= "http://localhost:8085/verefoo/dc";
	static final String URL_POLICY= "http://localhost:8085/verefoo/graph";

	
	
	@RequestMapping(method = RequestMethod.POST, value = "/register/insfrastructure")
	@ResponseBody
	public NFV registerInfrastructure(@RequestBody InfrastructureInfo info) {
		if(info==null||info.getMetadata()==null||info.getMetadata().getName()==null) {
			System.out.println("+++++++ Empty body");
			return null;
		}
		
		/*
		 * try { StringWriter stringWriter2 = new StringWriter(); JAXBContext jc =
		 * JAXBContext.newInstance("it.polito.verefoo.astrid.jaxb"); Marshaller m =
		 * jc.createMarshaller(); m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		 * Boolean.TRUE); m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
		 * "./xsd/astrid.xsd"); m.marshal(info, stringWriter2);
		 * System.out.println(stringWriter2.toString()); } catch (JAXBException e) {
		 * e.printStackTrace(); }
		 * 
		 */
		
		System.out.println("---"+info);
	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		
		System.out.println("+++++++ Obtained IN info: "+info.getMetadata().getName());
		RestTemplate restTemplate = new RestTemplate();
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		restTemplate.setMessageConverters(Arrays.asList(converter,new StringHttpMessageConverter()));
		// Data attached to the request.
		HttpEntity<InfrastructureInfo> requestBody = new HttpEntity<InfrastructureInfo>(info,headers);
		System.out.println("--- "+headers.toString());
		
		
		
		System.out.println("+++++++ Sending to Verekube");
		// Send request with POST method.
		NFV result = restTemplate.postForObject(URL_DC, requestBody, NFV.class);
		/*
		 * System.out.println("Status code:" + result.getStatusCode());
		 * 
		 * 
		 * // Code = 200. if (result.getStatusCode() == HttpStatus.OK) {
		 * System.out.println("+++++++ Success from Verekube"); }
		 * System.out.println("+++++++ Sending to Kube"); return result.getBody();
		 */
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/register/policy")
	@ResponseBody
	public String registerPolicy(@RequestBody String policy) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		
		System.out.println("------- Obtained Policy");
		RestTemplate restTemplate = new RestTemplate();
		
		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<>(policy,headers);

		System.out.println("------- Sending to Verekube");
		// Send request with POST method.
		ResponseEntity<String> result = restTemplate.postForEntity(URL_POLICY, requestBody, String.class);
		System.out.println("Status code:" + result.getStatusCode());
		// Code = 200.
		if (result.getStatusCode() == HttpStatus.OK) {
			System.out.println("------- Success from Verekube: "+result.getBody());
		}
		System.out.println("------- Printing");
		return result.getBody();
	}

}