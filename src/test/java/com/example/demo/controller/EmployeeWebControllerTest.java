package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeWebController.class)
class EmployeeWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private EmployeeService employeeService;

	@Test
	void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	void testHomeViewShowsEmployees() throws Exception {
		List<Employee> employees = asList(new Employee(1L, "test", 1000));
		when(employeeService.getAllEmployees()).thenReturn(employees);
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("employees", employees))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void testHomeViewShowsMessageWhenThereAreNoEmployees() throws Exception {

		when(employeeService.getAllEmployees()).thenReturn(emptyList());
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("employees", emptyList()))
				.andExpect(model().attribute("message", "No employee"));
	}

	@Test
	@DisplayName("Edit employee when it is found")
	void test1() throws Exception {
		Employee employee = new Employee(1L, "test", 1000);
		when(employeeService.getEmployeeById(1L)).thenReturn(employee);
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("employee", employee))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Edit employee when it is not found")
	void test2() throws Exception {
		when(employeeService.getEmployeeById(1L)).thenReturn(null);
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("employee", nullValue()))
				.andExpect(model().attribute("message",
						"No employee found with id 1"));
	}

	@Test
	@DisplayName("Edit new employee")
	void test3() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit"))
				.andExpect(model().attribute("employee", new Employee()))
				.andExpect(model().attribute("message", ""));
		verifyNoInteractions(employeeService);
	}

	@Test
	@DisplayName("Post employee without id should insert new employee")
	void test4() throws Exception {
		mvc.perform(post("/save").param("name", "test name").param("salary",
				"1000")).andExpect(view().name("redirect:/"));
		verify(employeeService)
				.insertNewEmployee(new Employee(null, "test name", 1000));
	}

	@Test
	@DisplayName("Post employee with id should update existing employee")
	void test5() throws Exception {
		mvc.perform(post("/save").param("id", "1").param("name", "test name")
				.param("salary", "1000")).andExpect(view().name("redirect:/")); // go
																				// back
																				// to
																				// the
																				// main
																				// page
		verify(employeeService).updateEmployeeById(1L,
				new Employee(1L, "test name", 1000));
	}
}
