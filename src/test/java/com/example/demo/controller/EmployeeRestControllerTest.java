package com.example.demo.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeRestController.class)
class EmployeeRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private EmployeeService employeeService;

	@Test
	void testAllEmployeesEmpty() throws Exception {
		this.mvc.perform(
				get("/api/employees").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	@Test
	void testAllEmployeesNotEmpty() throws Exception {
		when(employeeService.getAllEmployees()).thenReturn(asList(
				new Employee(1L, "gino", 333), new Employee(2L, "joe", 444)));

		this.mvc.perform(
				get("/api/employees").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("gino")))
				.andExpect(jsonPath("$[0].salary", is(333)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("joe")))
				.andExpect(jsonPath("$[1].salary", is(444)));

	}

	@Test
	void testOneEmployeeByIdWithExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong()))
				.thenReturn(new Employee(1L, "gino", 333));
		this.mvc.perform(
				get("/api/employees/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("gino")))
				.andExpect(jsonPath("$.salary", is(333)));
	}
	
	@Test
	void testOneEmployeeByIdWithNonExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong()))
				.thenReturn(null);
		this.mvc.perform(
				get("/api/employees/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}
}
