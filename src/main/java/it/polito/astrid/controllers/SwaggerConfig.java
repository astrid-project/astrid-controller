package it.polito.astrid.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private static final String description = "Astrid Controller is  the core element of the ASTRID conceptual architecture, including both service\n"
			+ "management and situational awareness. Starting from the descriptive and applicative semantics of the Context Model, controller is expected to deploy and manage the life-time of the service, by adapting the awareness layer of individual components and the whole service graph according to specific needs of detection algorithms. This means that monitoring operations, types and frequency of event reporting, level of logging is selectively and locally adjusted to retrieve the exact amount of knowledge, without overwhelming the whole system with unnecessary information. The purpose is to get more details for critical or vulnerable components when anomalies are detected that may indicate an attack, or when a warning is issued by cyber-security teams about new threats and vulnerabilities just discovered.\n"
			+ "The supported actions, of astrid controller,  includes: set and change at run-time security configuration (logging, packet filtering rules), de-provision the service, add/replace/delete virtual functions in the service graph to remove vulnerabilities or compromised entities, divert traffic for legal interception.";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("it.polito.astrid.controllers")).paths(PathSelectors.any()).build();
	}
	
	private ApiInfo apiInfo() {
		ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
		apiInfoBuilder.title("ASTRID Controller");
		apiInfoBuilder.description(description);
		apiInfoBuilder.version("0.0.1-SNAPSHOT");
		return apiInfoBuilder.build();
	}
	
}
