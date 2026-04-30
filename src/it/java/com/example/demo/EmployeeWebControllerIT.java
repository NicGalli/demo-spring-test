package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeWebControllerIT {

	@Autowired
	private EmployeeRepository employeeRepository;
	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}

	@AfterEach
	void teardown() {
		driver.quit();
	}

	@Test
	@DisplayName("home page")
	void test1() {
		Employee testEmployee = employeeRepository
				.save(new Employee(null, "test employee", 1000));
		driver.get(baseUrl);

		assertThat(driver.findElement(By.id("employee_table")).getText())
				.contains("test employee", "1000", "Edit");

		driver.findElement(By
				.cssSelector("a[href*='/edit/" + testEmployee.getId() + "']"));
	}

	@Test
	@DisplayName("Edit Page with new employee")
	void test2() {
		driver.get(baseUrl + "/new");
		driver.findElement(By.name("name")).sendKeys("new employee");
		driver.findElement(By.name("salary")).sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(employeeRepository.findByName("new employee").getSalary())
				.isEqualTo(2000L);
	}

	@Test
	@DisplayName("Edit Page update employee")
	void test3() {
		Employee testEmployee = employeeRepository
				.save(new Employee(null, "test employee", 1000));
		driver.get(baseUrl + "/edit/" + testEmployee.getId());

		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("modified employee");

		final WebElement salaryField = driver.findElement(By.name("salary"));
		salaryField.clear();
		salaryField.sendKeys("2000");

		driver.findElement(By.name("btn_submit")).click();

		assertThat(
				employeeRepository.findByName("modified employee").getSalary())
				.isEqualTo(2000L);
	}
}
