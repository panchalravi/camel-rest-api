package com.lab.rest.webservices.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lab.rest.webservices.model.Student;

@Service
public class StudentService {
	private static final List<Student> students = new ArrayList<>();
	private static int counter = 3;
	
	static {
		students.add(new Student(1, "Ravi Panchal", "Electronics"));
		students.add(new Student(2, "Prashant Shah", "Mechanics"));
		students.add(new Student(3, "Tom Crawly", "Science"));
	}

	public List<Student> findAll() {
		return students;
	}
	
	public Student save(Student student) {
		student.setId(++counter);
		students.add(student);
		return student;
	}
}
