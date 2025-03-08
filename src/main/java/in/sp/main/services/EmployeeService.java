package in.sp.main.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Course;
import in.sp.main.entities.Employee;
import in.sp.main.repositoryies.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public boolean loginEmpServices(String email,String password)
	{
		Employee employee = employeeRepository.findByEmail(email);
		if(employee != null)
		{
			return password.equals(employee.getPassword());
		}
		return false;
	}

	public List<Employee> getAllEmployeeDetails() 
	{
		return employeeRepository.findAll();
	} 
	
	public void addEmployee(Employee employee)
	{
		employeeRepository.save(employee);
	}
	
	public Employee getEmployeeDetails(String employeeName)
	{
		return employeeRepository.findByName(employeeName);
	}
	
	public void updateEmployeeDetails(Employee employee)
	{
		employeeRepository.save(employee);
	}
	public Page<Employee> getAllEmployeeDetailsByPagination(Pageable pageable) 
	{
		return employeeRepository.findAll(pageable);
	}
	public void deleteEmployeeDetails(String employeeName)
	{
		Employee employee= employeeRepository.findByName(employeeName);
		if(employee != null)
		{
			employeeRepository.delete(employee);
		}else {
			 throw new RuntimeException("Employee Not Found With Course Name: " + employeeName);
		}
	}
}
