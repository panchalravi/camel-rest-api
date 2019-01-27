package com.lab.rest.webservices.exception;

import java.util.Date;
import java.util.List;

public class ExceptionResponse {

	private Date timestamp;
	private List<ErrorDetails> errors;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<ErrorDetails> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDetails> errors) {
		this.errors = errors;
	}

}
