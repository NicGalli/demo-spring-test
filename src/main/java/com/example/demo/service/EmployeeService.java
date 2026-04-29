package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class EmployeeService {
	private Map<Long, Employee> employees = new LinkedHashMap<>();

	public EmployeeService() {
		employees.put(1L, new Employee(1L, "mario", 1000));
		employees.put(2L, new Employee(2L, "gino", 1000));

	}

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Employee> getAllEmployees() {
		return new LinkedList<>(employees.values());
	}

	public Employee getEmployeeById(long id) {
		return employees.get(id);
	}

	public Employee insertNewEmployee(Employee employee) {
		employee.setId(employees.size() + 1L);
		employees.put(employee.getId(), employee);
		return employee;
	}

	public Employee updateEmployeeById(long id, Employee employee) {
		employee.setId(id);
		employees.put(employee.getId(), employee);
		return employee;
	}

}
