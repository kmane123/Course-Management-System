package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Employee;
import in.sp.main.entities.EmployeeOrder;
import in.sp.main.entities.Orders;
import in.sp.main.repositoryies.EmployeeOrderRepository;
import in.sp.main.repositoryies.EmployeeRepository;
import in.sp.main.services.CourseService;
import in.sp.main.services.EmployeeService;
import in.sp.main.services.OrderServices;

@Controller
@SessionAttributes("sessionEmp")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeServices;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CourseService courseServices;
	
	@Autowired
	private OrderServices orderServices;
	
	@Autowired
	private EmployeeOrderRepository employeeOrderRepository;

	@GetMapping("/employeeLogin")
	public String openEmployeeLoginPage() {
		return "employee-login";
	}

	@GetMapping("/employeeProfile")
	public String openEmployeeProfilePage(Model model) {
		if (model.containsAttribute("sessionEmp")) {
			return "emp-profile";
		}
		return "redirect:/employeeLogin";
	}

	@PostMapping("/employeeLoginForm")
	public String openEmployeeLoginForm(Model model, @RequestParam("employeeemail") String email,
			@RequestParam("employeepass") String pass) {

		boolean isAuthonticated = employeeServices.loginEmpServices(email, pass);
		if (isAuthonticated) {
			Employee isAutonticatedEmp = employeeRepository.findByEmail(email);
			model.addAttribute("sessionEmp", isAutonticatedEmp);
			return "emp-profile";
		} else {
			model.addAttribute("errorMsg", "Email id and password doesnt match");
			return "employee-login";
		}
	}

	@GetMapping("/employeeManagement")
	public String openEmployeeManagement(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<Employee> employeePage = employeeServices.getAllEmployeeDetailsByPagination(pageable);

		model.addAttribute("employeePage", employeePage);

		return "employee-management";
	}

	@GetMapping("/addEmployee")
	public String openAddEmployeePage(Model model) {
		model.addAttribute("employee", new Employee());
		return "add-employee";
	}

	@PostMapping("/addEmployeeForm")
	public String addEmployeeFormPage(@ModelAttribute("employee") Employee employee, Model model) {
		try {
			employeeServices.addEmployee(employee);
			model.addAttribute("successMsg", "Employee add successfully");

		} catch (Exception e) {
			model.addAttribute("errorMsg", "Employee not add successfully");
			e.printStackTrace();
		}
		return "add-employee";
	}

	@GetMapping("/editEmployee")
	public String editEmployee(@RequestParam("employeeName") String employeeName, Model model) {
		Employee employee = employeeServices.getEmployeeDetails(employeeName);
		model.addAttribute("employee", employee);
		model.addAttribute("newEmployeeObj", new Employee());
		return "edit-employee";
	}

	@PostMapping("/updateEmployeeDetails")
	public String updateEmployeeDetails(@ModelAttribute("newEmployeeObj") Employee newEmployeeObj,
			RedirectAttributes redirectAttributes) {
		try {
			Employee oldEmployeeObj = employeeServices.getEmployeeDetails(newEmployeeObj.getName());
			newEmployeeObj.setId(oldEmployeeObj.getId());
			employeeServices.updateEmployeeDetails(newEmployeeObj);
			redirectAttributes.addFlashAttribute("successMsg", "Employee details Updated successfully");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Employee details Not Updated Successfully");
			e.printStackTrace();
		}
		return "redirect:/employeeManagement";
	}

	@GetMapping("/deleteEmployeeDetails")
	public String deleteEmployeeDetails(@RequestParam("employeeName") String employeeName,
			RedirectAttributes redirectAttributes) {
		try {
			employeeServices.deleteEmployeeDetails(employeeName);
			redirectAttributes.addFlashAttribute("successMsg", "Course Deleted Successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Course Not Deleted Successfully");
			e.printStackTrace();
		}
		return "redirect:/employeeManagement";

	}

	// -------------open sell course page------------------------
	@GetMapping("/sellCourse")
	public String openSellCoursePage(Model model, Orders order)
	{
		List<String> courseName = courseServices.getCourseName();
		model.addAttribute("courseName",courseName);
		
		UUID randomUUID = UUID.randomUUID();
		String orderId = randomUUID.toString();
		
		model.addAttribute("orderId", orderId);
		model.addAttribute("order", order);
		
		return "sell-course";
	}
	
	@PostMapping("/sellCourseForm")
	public String sellCourseForm(@ModelAttribute("order")Orders order,@ModelAttribute("sessionEmp")Employee sessionEmp, RedirectAttributes redirectAttributes)
	{
		LocalDate ld = LocalDate.now();
		String pdate = ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime lt = LocalTime.now();
		String ptime = lt.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
		
		String purchased_date_time = pdate+", "+ptime;
		
		order.setDateOfPurchase(purchased_date_time);

		
		try{
			orderServices.storeUserOrders(order);
			
			EmployeeOrder employeeOrder = new EmployeeOrder();
			employeeOrder.setOrderId(order.getOrderId());
			employeeOrder.setEmployeeEmail(sessionEmp.getEmail());
			employeeOrderRepository.save(employeeOrder);
			
			redirectAttributes.addFlashAttribute("successMsg","Course Provided Successfully");
		}catch(Exception e)
		{
			redirectAttributes.addFlashAttribute("errorMsg","Course Not Provided Successfully");
			e.printStackTrace();
		}
		
		return "redirect:/sellCourse";
	}
	
	

	// -------------inquiry management------------------------
	@GetMapping("/inquiryManagement")
	public String openIquiryManagementPage(SessionStatus sessionStatus) {
		return "inquiry-management";
	}

	// -------------employee logout------------------------

	@GetMapping("/employeeLogout")
	public String employeeLogout(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "employee-login";
	}

}
