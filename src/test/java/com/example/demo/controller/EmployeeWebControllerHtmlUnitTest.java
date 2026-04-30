package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeWebController.class)
class EmployeeWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private EmployeeService employeeService;

	@Test
	void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Employees");
	}

	@Test
	void testHomePageWithNoEmployees() throws Exception {
		when(employeeService.getAllEmployees()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).contains("No employee");
	}

	@Test
	void testHomePageWithEmployeesShouldShowThemInATable() throws Exception {
		when(employeeService.getAllEmployees())
				.thenReturn(asList(new Employee(1L, "test1", 1000),
						new Employee(2L, "test2", 2000)));
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getBody().getTextContent())
				.doesNotContain("No employee");
		HtmlTable table = page.getHtmlElementById("employee_table");
		assertThat(table.asNormalizedText()).isEqualTo("Employees\n"
				+ "ID	Name	Salary\n" + "1	test1	1000	Edit\n"
				+ "2	test2	2000	Edit");
		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");
	}

	@Test
	void testEditNonExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/edit/1");
		assertThat(page.getBody().getTextContent())
				.contains("No employee found with id 1");
	}

	@Test
	void testEditExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(1L))
				.thenReturn(new Employee(1L, "original name", 1000));
		HtmlPage page = webClient.getPage("/edit/1");

		final HtmlForm form = page.getFormByName("employee_form");
		form.getInputByValue("original name")
				.setValueAttribute("modified name");
		form.getInputByValue("1000").setValueAttribute("2000");

		form.getButtonByName("btn_submit").click();

		verify(employeeService).updateEmployeeById(1L,
				new Employee(1L, "modified name", 2000));
	}

	@Test
	void testEditNewEmployee() throws Exception {
		HtmlPage page = webClient.getPage("/new");

		final HtmlForm form = page.getFormByName("employee_form");
		form.getInputByName("name").setValueAttribute("new name");
		form.getInputByName("salary").setValueAttribute("1000");

		form.getButtonByName("btn_submit").click();

		verify(employeeService)
				.insertNewEmployee(new Employee(null, "new name", 1000));
	}

	@Test
	void testHomePageShouldProvideALinkForCreatingANewEmployee()
			throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getAnchorByText("New employee").getHrefAttribute())
				.isEqualTo("/new");
	}
}
