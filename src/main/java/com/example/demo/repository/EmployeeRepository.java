package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Employee;

public interface EmployeeRepository {

	List<Employee> findAll();
	Optional<Employee> findById(long id);
	Employee save(Employee employee);
}
