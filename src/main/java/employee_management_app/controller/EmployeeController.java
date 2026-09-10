package employee_management_app.controller;

import employee_management_app.model.Employee;
import employee_management_app.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }
  
@PostMapping
public ResponseEntity<Employee> createEmployee(
        @Valid @RequestBody Employee employee) {

    Employee createdEmployee = employeeService.createEmployee(employee);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdEmployee);
}

@PutMapping("/{id}")
public Employee updateEmployee(
        @PathVariable Long id,
        @RequestBody Employee employee) {

    return employeeService.updateEmployee(id, employee);
    }
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

    employeeService.deleteEmployee(id);

    return ResponseEntity.noContent().build();
}

}
