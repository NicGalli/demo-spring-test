package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;

@Repository
public class EmployeeRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary Implementation";

	public List<Employee> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);

	}

	public Optional<Employee> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);

	}

	public Employee save(Employee employee) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);

	}
}
