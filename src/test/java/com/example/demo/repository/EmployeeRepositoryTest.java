package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository repository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	void firstLearningTest() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = repository.save(employee);
		Collection<Employee> employees = repository.findAll();
		assertThat(employees).containsExactly(saved);
	}

	@Test
	void secondLearningTest() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = entityManager.persistFlushFind(employee);
		Collection<Employee> employees = repository.findAll();
		assertThat(employees).containsExactly(saved);
	}

	@Test
	void testFindByEmployeeName() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = entityManager.persistFlushFind(employee);
		Employee found=repository.findByName("test");
		assertThat(found).isEqualTo(saved);
	}

}
