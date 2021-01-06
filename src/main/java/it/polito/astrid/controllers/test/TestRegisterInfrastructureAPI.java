package it.polito.astrid.controllers.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/*
 * This class runs some tests in order to check the behavior of the Astrid controller when receives request to registerInfrastructureAPI
 */

@AutoConfigureMockMvc
@WebMvcTest
public class TestRegisterInfrastructureAPI {
	
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
	public void test_registerInfrastructureAPI_GETRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_DELETERequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_PUTRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_PATCHRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_HEADRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.head("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_OPTIONSRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.options("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_POSTRequestWithoutBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_POSTRequestWithoutCorrectBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/insfrastructure")
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
	public void test_registerInfrastructureAPI_POSTRequestWithCorrectBodybutNotValidated() throws Exception {
		String infrastructureEvent = sm.getInfrastructureEventXMLFormat();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/insfrastructure")
							.content(infrastructureEvent)
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
	public void test_registerInfrastructureAPI_POSTRequestCorrectly() throws Exception {
		String infrastructureInfo = sm.getInfrastructureInfoXMLFormat();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/insfrastructure")
							.content(infrastructureInfo)
							.contentType(MediaType.APPLICATION_XML)
							.accept(MediaType.APPLICATION_XML))
							.andExpect(status().isOk())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
}
