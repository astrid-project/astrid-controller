package it.polito.astrid.controllers.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

/*
 * This class runs some tests in order to check the behavior of the Astrid controller when receives request to registerEventAPI
 */

@AutoConfigureMockMvc
@WebMvcTest
public class TestRegisterEventAPI {
	
	@Autowired
    private MockMvc mockMvc;
	
	private ServicesMethods sm = new ServicesMethods();
	
	/*
	 * 
	 * Testing send a GET request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_GETRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	
	/*
	 * 
	 * Testing send a DELETE request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_DELETERequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a PUT request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_PUTRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a PATCH request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_PATCHRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a HEAD request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_HEADRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.head("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a OPTIONS request without any body;
	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerEventAPI_OPTIONSRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.options("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly but without any body;
	 * Expected answer not null with HTTP error 415 - Unsupported Media Type
	 *
	 */
	@Test
	public void test_registerEventAPI_POSTRequestWithoutBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/event")
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isUnsupportedMediaType())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly but without a correct body type;
	 * Expected answer not null with HTTP error 415 - Unsupported Media Type
	 *
	 */
	@Test
	public void test_registerEventAPI_POSTRequestWithoutCorrectBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/event")
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isUnsupportedMediaType())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly with a correct body type but non valid;
	 * Expected answer not null with HTTP error 400 - Bad Request 
	 *
	 */
	@Test
	public void test_registerEventAPI_POSTRequestWithoutCorrectBodybutNotValidated() throws Exception {
		String infrastructureInfo = sm.getInfrastructureInfoXMLFormat();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/event")
							.content(infrastructureInfo)
							.contentType(MediaType.APPLICATION_XML)
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isBadRequest())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly with a correct body;
	 * Expected answer not null with HTTP status 200 - OK
	 *
	 */
	@Test
	public void test_registerEventAPI_POSTRequestCorrectly() throws Exception {
		String infrastructureEvent = sm.getInfrastructureEventXMLFormat();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/event")
							.content(infrastructureEvent)
							.contentType(MediaType.APPLICATION_XML)
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isOk())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly with a correct body;
	 * Expected answer not null with HTTP status 200 - OK
	 *
	 */
	/*@Test
	public void test_registerAttackEventAPI_POSTRequestCorrectly() throws Exception {
		String infrastructureEvent = sm.getAttackEventXMLFormat();
		/*HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestBody = new HttpEntity<>(infrastructureEvent, headers);
		ResponseEntity<String> result = null;*/
		//int i=0;
		//MvcResult res;
		/*for (i=0;i<99;i++) {
			/*try {
				result = restTemplate.postForEntity("http://localhost:8083/register/attack", requestBody, String.class);
			}catch(Exception e) {
				
			}
			res = mockMvc.perform(MockMvcRequestBuilders.post("/register/attack")
					.content(infrastructureEvent)
					.contentType(MediaType.APPLICATION_XML)
					.accept(MediaType.TEXT_PLAIN))
					.andExpect(status().isOk())
					.andReturn();
		}
		MvcResult res = mockMvc.perform(MockMvcRequestBuilders.post("/register/attack")
							.content(infrastructureEvent)
							.contentType(MediaType.APPLICATION_XML)
							.accept(MediaType.TEXT_PLAIN))
							.andExpect(status().isOk())
							.andReturn();
		String resultSS = res.getResponse().getContentAsString();
		assertNotNull(resultSS);
		//assertEquals(resultSS, 100);
	}*/

}
