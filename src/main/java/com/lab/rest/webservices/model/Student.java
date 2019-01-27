package com.lab.rest.webservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Student {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull(message="Name is required")
	@Size(min=2, message="Name should be minimum 2 characters long")
	private String name;
	
	@NotNull(message="Subject is required")
	@NotEmpty(message="Subject cannot be empty")
	private String subject;
	
	
	public Student() {
		super();
	}

	public Student(long id, String name, String subject) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
