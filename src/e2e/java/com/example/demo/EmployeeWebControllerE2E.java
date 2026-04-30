package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.bonigarcia.wdm.WebDriverManager;

class EmployeeWebControllerE2E {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmployeeWebControllerE2E.class);

	private static int port = Integer
			.parseInt(System.getProperty("server.port", "8080"));

	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeAll
	static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new FirefoxDriver();
	}

	@AfterEach
	void teardown() {
		driver.quit();
	}

	@Test
	void testHomePage() {
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/new"));
	}

	@Test
	void testCreateNewEmployee() {
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		driver.get(baseUrl + "/new");
		driver.findElement(By.name("name")).sendKeys("new employee");
		driver.findElement(By.name("salary")).sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("employee_table")).getText())
				.contains("new employee", "2000");
	}

	@Test
	@DisplayName("update employee")
	void test() throws JSONException {
		String id = postEmployee("employee to edit", 2000);

		driver.get(baseUrl);

		driver.findElement(By.cssSelector("a[href*='/edit/" + id + "']"))
				.click();

		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("modified employee");

		final WebElement salaryField = driver.findElement(By.name("salary"));
		salaryField.clear();
		salaryField.sendKeys("3000");

		driver.findElement(By.name("btn_submit")).click();

		assertThat(driver.findElement(By.id("employee_table")).getText())
				.contains(id, "modified employee", "3000");
	}

	private String postEmployee(String name, int salary) throws JSONException {
		JSONObject body = new JSONObject();
		body.put("name", name);
		body.put("salary", salary);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

		ResponseEntity<String> answer = new RestTemplate().postForEntity(
				baseUrl + "/api/employees/new", entity, String.class);
		LOGGER.debug("answer for POST: " + answer);
		return new JSONObject(answer.getBody()).get("id").toString();
	}

}
