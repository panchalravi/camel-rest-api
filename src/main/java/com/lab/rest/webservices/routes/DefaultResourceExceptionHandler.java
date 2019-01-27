package com.lab.rest.webservices.routes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.lab.rest.webservices.exception.ErrorDetails;
import com.lab.rest.webservices.exception.ExceptionResponse;

@Component
public class DefaultResourceExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultResourceExceptionHandler.class);

	@Autowired
	private Environment env;
	
	@Handler
	public void handleBeanValidationException(Exchange exchange) {
		Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
		
		Set<ConstraintViolation<Object>> violations = ((BeanValidationException) exception).getConstraintViolations();
		List<ErrorDetails> errorDetails = new ArrayList<>();
		for(ConstraintViolation<Object> violation: violations) {
			errorDetails.add(new ErrorDetails(violation.getPropertyPath().toString(), violation.getMessage()));
		}
		
		setExceptionResponse(exchange, errorDetails, HttpStatus.BAD_REQUEST.value());
	}

	
	@Handler
	public void handleParseException(Exchange exchange) {
		List<ErrorDetails> errorDetails = new ArrayList<>();
		errorDetails.add(new ErrorDetails("request_payload_error", env.getProperty("request_payload_error")));
		
		setExceptionResponse(exchange, errorDetails, HttpStatus.BAD_REQUEST.value());
	}
	
	private void setExceptionResponse(Exchange exchange, List<ErrorDetails> errorDetails, int httpStatusCode) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setTimestamp(new Date());
		exceptionResponse.setErrors(errorDetails);
		
		exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatusCode);
		exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		exchange.getOut().setBody(exceptionResponse);
	}

}
