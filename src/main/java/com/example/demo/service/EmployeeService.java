package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	Employee getEmployeeById(long id);
	Employee insertNewEmployee(Employee employee);
	Employee updateEmployeeById(long id, Employee replacement);
}
