package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class EmployeeJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Employee saved = entityManager
				.persistFlushFind(new Employee(null, "test", 1000));
		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getSalary()).isEqualTo(1000);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
		LoggerFactory.getLogger(EmployeeJpaTest.class)
				.info("Saved: " + saved.toString());
	}

}
