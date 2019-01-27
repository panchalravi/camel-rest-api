package com.lab.rest.webservices.routes;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.rest.webservices.model.Student;
import com.lab.rest.webservices.service.StudentService;

@Component
public class DefaultRestResources extends RouteBuilder {
	
	@Autowired
	private ObjectMapper objectMapper; 

	@Override
	public void configure() throws Exception {

		restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

		onException(BeanValidationException.class).handled(true)
			.bean(DefaultResourceExceptionHandler.class, "handleBeanValidationException")
			.marshal().json(JsonLibrary.Jackson);
		
		onException(JsonParseException.class).handled(true)
			.bean(DefaultResourceExceptionHandler.class, "handleParseException")
			.marshal().json(JsonLibrary.Jackson);
		
		interceptFrom()
			.when(simple("${routeId} != 'audit-message'"))
			.process(e -> {
			ProducerTemplate producerTemplate = e.getContext().createProducerTemplate();
			producerTemplate.asyncSend("direct:audit", e);
			//log.info("Received:{} {}", e.getExchangeId(), objectMapper.writeValueAsString(e.getIn().getBody()));
			//e.getIn().getBody();
		});
		
		rest("/students").produces("application/json")
			.get().route().bean(StudentService.class, "findAll").endRest()
			.post().type(Student.class).route().to("bean-validator:resourcevalidator")
				.to("jpa:com.lab.rest.webservices.model.Student")
				.log("Inserted new student ${body.id}");
				//.bean(StudentService.class, "save");
		
		from("direct:audit").routeId("audit-message").threads(5).log("Received: ${body}");
	}

}
