package in.sp.main.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.entities.Inquiry;
import in.sp.main.services.InquiryServices;

@RestController
@RequestMapping("/api")
public class InquiryApi 
{

	@Autowired
	private InquiryServices inquiryServices;
	
	@GetMapping("/searchInquiries")
	public List<Inquiry> searchInquiries(@RequestParam("phoneNumber")String phoneNumber)
	{
		return inquiryServices.findInquiriesUsingPhno(phoneNumber);
	}
}
