package in.sp.main.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sp.main.services.OrderChartServices;

@Controller
public class OrderChartController 
{
	@Autowired OrderChartServices orderChartServices;

	@GetMapping("/adminProfile")
	public String openAdminProfilePage(Model model) 
	{
		//-----first chart--------------
       List<Object[]> listOfCoursesAmountSales = orderChartServices.findCoursesAmountTotalSales();
		
		List<String> date11 = new ArrayList<>();
		List<Double> totalAmount11 = new ArrayList<>();
		
		for(Object[] obj : listOfCoursesAmountSales)
		{
			date11.add((String) obj[0]);
			totalAmount11.add((Double) obj[1]);
		}
		
		model.addAttribute("date11", date11);
		model.addAttribute("totalAmount11", totalAmount11);
		
		//-----------Second chart-----------------------
		
		List<Object[]> listOfCoursesTotalSales =orderChartServices.findCoursesTotalSales();
		
		List<String> subName = new ArrayList<>();
		List<Long> totalCourse = new ArrayList<>();
		
		for(Object[] obj : listOfCoursesTotalSales)
		{
			 subName.add((String)obj[0]);
			 totalCourse.add((Long)obj[1]);
		}
		model.addAttribute("subName",subName);
		model.addAttribute("totalCourse",totalCourse);
		
		//-----------Thired chart-----------------------
		
		List<Object[]> listOfCourseSoldPerDay = orderChartServices.findCourseSoldPerDay();
		
		List<String> date1 = new ArrayList<>();
		List<Long> total1 = new ArrayList<>();
		
		for(Object[] obj:listOfCourseSoldPerDay)
		{
			date1.add((String)obj[0]);
			total1.add((Long)obj[1]);
		}
		
		model.addAttribute("date1",date1);
		model.addAttribute("total1",total1);
		
		return "admin-profile";
	}
}
