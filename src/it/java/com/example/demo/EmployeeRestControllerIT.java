package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeRestControllerIT {

	@Autowired
	private EmployeeRepository employeeRepository;
	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}

	@Test
	@DisplayName("Test new employee")
	void test1() {
		Response response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Employee(null, "new employee", 1000)).when()
				.post("/api/employees/new");

		Employee saved = response.getBody().as(Employee.class);
		assertThat(employeeRepository.findById(saved.getId())).contains(saved);
	}

	@Test
	@DisplayName("Test update employee")
	void test2() {
		Employee saved = employeeRepository
				.save(new Employee(null, "new employee", 1000));
		
		given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Employee(null, "modified name", 2000)).when()
				.put("/api/employees/update/" + saved.getId()).then()
				.statusCode(200).body("id", equalTo(saved.getId().intValue()),
						"name", equalTo("modified name"), "salary",
						equalTo(2000));

	}

}
