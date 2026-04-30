package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@Controller
public class EmployeeWebController {

	private static final String MESSAGE = "message";
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/")
	public String index(Model model) {
		List<Employee> allEmployees = employeeService.getAllEmployees();
		model.addAttribute("employees", allEmployees);
		model.addAttribute(MESSAGE,
				allEmployees.isEmpty() ? "No employee" : "");
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable long id) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		model.addAttribute(MESSAGE,
				employee == null ? "No employee found with id " + id : "");
		return "edit";
	}

	@GetMapping("/new")
	public String newEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute(MESSAGE, "");
		return "edit";
	}

	@PostMapping("/save")
	public String saveEmployee(Employee employee) {
		final Long id = employee.getId();
		if (id == null) {
			employeeService.insertNewEmployee(employee);
		} else {
			employeeService.updateEmployeeById(id, employee);
		}
		return "redirect:/";
	}
}
