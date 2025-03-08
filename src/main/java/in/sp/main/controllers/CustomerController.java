package in.sp.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.User;
import in.sp.main.repositoryies.OrdersRepository;
import in.sp.main.services.CustomerServices;

@Controller
public class CustomerController 
{
 
	@Autowired
	private CustomerServices customerServices;
	
	@Autowired
	 private OrdersRepository ordersRepository;
	
	@GetMapping("/customerManagement")
	public String getAllCustomerDetails(Model model,@RequestParam(name="page",defaultValue="0")int page,
			                             @RequestParam(name="size",defaultValue="3")int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<User> userPage = customerServices.getAllUserDetailsByPageable(pageable);
		model.addAttribute("userPage",userPage);
		
		return "customer-management";
	}
	
	@GetMapping("/userCoursesDetails")
	public String getAllCourseDetails(Model model, @RequestParam("userEmail")String email,
			                            @RequestParam("userName") String userName)
	{
		List<Object[]> coursesList = ordersRepository.findCustomerCoursesByEmail(email);
		model.addAttribute("coursesList",coursesList);
		model.addAttribute("userName",userName);
		
		return"user-course";
	}
	
	@GetMapping("/banUserDetails")
	public String banUserDetails(@RequestParam("userEmail")String email,@RequestParam("banStatus")boolean banStatus,RedirectAttributes redirectAttributes)
	{
		try {
			  User userObject = customerServices.getAllDetailsByEmail(email);
			  userObject.setBanStatus(banStatus);
			  customerServices.updateUserBanStatus(userObject);
			  
			  if(banStatus)
			  {
				  redirectAttributes.addFlashAttribute("successMsg","User banned Successfully");
			  }else {
				  redirectAttributes.addFlashAttribute("errorMsg","User UnBanned Successfully");
			  }
			
		}catch(Exception e)
		{
		   redirectAttributes.addFlashAttribute("errorMsg","User Not banned due to some error");
		   e.printStackTrace();	
		}
		
		return "redirect:/customerManagement";
	}
}
