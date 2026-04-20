package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class EmployeeService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary Implementation";

	public List<Employee> getAllEmployees() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Employee getEmployeeById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
