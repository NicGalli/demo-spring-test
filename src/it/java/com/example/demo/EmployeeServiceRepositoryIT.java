package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(EmployeeService.class)
class EmployeeServiceRepositoryIT {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	@DisplayName("Test service can insert into repository")
	void test1() {
		Employee saved = employeeService
				.insertNewEmployee(new Employee(null, "an employee", 1000));
		employeeRepository.findById(saved.getId()).isPresent();
	}

	@Test
	@DisplayName("Test service can update repository")
	void test2() {
		Employee saved = employeeRepository
				.save(new Employee(null, "an employee", 1000));
		Employee modified = employeeService.updateEmployeeById(saved.getId(),
				new Employee(null, "modified", 2000));
		assertThat(employeeRepository.findById(saved.getId()))
				.contains(modified);
	}

}
