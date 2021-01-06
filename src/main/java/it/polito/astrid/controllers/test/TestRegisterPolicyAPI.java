package it.polito.astrid.controllers.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/*
 * This class runs some tests in order to check the behavior of the Astrid controller when receives request to registerPolicyAPI
 */

@AutoConfigureMockMvc
@WebMvcTest
public class TestRegisterPolicyAPI {
	
	@Autowired
    private MockMvc mockMvc;
	
	/*
	 * 
	 * Testing send a GET request without any body;
//	 * Expected answer not null with HTTP error 405 - Method Not Allowed
	 *
	 */
	@Test
	public void test_registerPolicyAPI_GETRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_DELETERequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_PUTRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_PATCHRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_HEADRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.head("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_OPTIONSRequest() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.options("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
							.andExpect(status().isMethodNotAllowed())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
//	 * Testing send a POST request correctly but without any body;
	 * Expected answer not null with HTTP error 415 - Unsupported Media Type
	 *
	 */
	@Test
	public void test_registerPolicyAPI_POSTRequestWithoutBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/policy")
							.accept(MediaType.TEXT_PLAIN))
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
	public void test_registerPolicyAPI_POSTRequestWithoutCorrectBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/policy")
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.TEXT_PLAIN))
							.andExpect(status().isUnsupportedMediaType())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}
	
	/*
	 * 
	 * Testing send a POST request correctly with a not well formed body;
	 * Expected answer not null with HTTP status 502 - Bad Gateway
	 *
	 */
	@Test
	public void test_registerPolicyAPI_POSTRequestWithNotWellFormedBody() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/policy")
							.content("deny all")
							.contentType(MediaType.TEXT_PLAIN)
							.accept(MediaType.TEXT_PLAIN))
							.andExpect(status().isBadGateway())
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
	public void test_registerPolicyAPI_POSTRequestCorrectly() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/policy")
							.content("deny all")
							.contentType(MediaType.TEXT_PLAIN)
							.accept(MediaType.TEXT_PLAIN))
							.andExpect(status().isOk())
							.andReturn();
		String resultSS = result.getResponse().getContentAsString();
		assertNotNull(resultSS);
	}

}
