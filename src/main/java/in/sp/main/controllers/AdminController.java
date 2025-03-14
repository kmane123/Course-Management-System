package in.sp.main.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Course;
import in.sp.main.entities.Feedback;
import in.sp.main.services.CourseService;
import in.sp.main.services.FeedbackService;

@Controller
public class AdminController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private FeedbackService feedbackService;

	private String UPLOAD_DIR = "src/main/resources/static/uploads/";
	private String IMAGE_URL = "http://localhost:8080/uploads/";

	@GetMapping("/adminLogin")
	public String openAdminLoginPage() 
	{
		return "admin-login";
	}

	@PostMapping("/adminLoginForm")
	public String AdminLoginForm(@RequestParam("adminemail") String aemail, @RequestParam("adminpass") String apass,
			Model model) {
		if (aemail.equals("admin@gmail.com") && apass.equals("admin123")) {

			return "admin-profile";
		} else {
			model.addAttribute("errroMsg", "Invalid login Id & password");

			return "admin-login";
		}
	}

	@GetMapping("/courseManagement")
	public String openCourseManagementPage(Model model,
					@RequestParam(name="page", defaultValue = "0") int page,
					@RequestParam(name="size", defaultValue = "4") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Course> coursesPage = courseService.getAllCourseDetailsByPagination(pageable);
		
		model.addAttribute("coursesPage", coursesPage);
		
		return "course-management";
	}
	


	@GetMapping("/addCourse")
	public String openAddCoursePage(Model model) {
		model.addAttribute("course", new Course());
		return "add-course";
	}

	@PostMapping("/addCourseForm")
	public String addCourseForm(@ModelAttribute("course") Course course,
			@RequestParam("courseImg") MultipartFile courseImg, Model model) {
		try {
			courseService.addCourse(course, courseImg);
			model.addAttribute("successMsg", "Course Add Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Course Not Added Successfully");
		}
		return "add-course";
	}

	@GetMapping("/editCourse")
	public String openEditCoursePage(@RequestParam("courseName") String courseName, Model model) {
		Course course = courseService.getCourseDetails(courseName);
		model.addAttribute("course", course);
		model.addAttribute("newCourseObj", new Course());
		return "edit-course";
	}

	@PostMapping("/updateCourseDetailsForm")
	public String updateCourseDetailsForm(@ModelAttribute("newCourseObj") Course newCourseObj,
			@RequestParam("courseImg") MultipartFile courseImg, RedirectAttributes redirectAttributes) {
		try {
			Course oldCourseObj = courseService.getCourseDetails(newCourseObj.getName());
			newCourseObj.setId(oldCourseObj.getId());

			if (!courseImg.isEmpty()) {
				String imgName = courseImg.getOriginalFilename();
				Path imgPath = Paths.get(UPLOAD_DIR + imgName);
				Files.write(imgPath, courseImg.getBytes());

				String imgUrl = IMAGE_URL + imgName;
				newCourseObj.setImageUrl(imgUrl);

			} else {
				newCourseObj.setImageUrl(oldCourseObj.getImageUrl());
			}

			courseService.updateCourseDetails(newCourseObj);
			redirectAttributes.addFlashAttribute("successMsg", "Course Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Course Not Updated Successfully");
		}

		return "redirect:/courseManagement";
	}

	@GetMapping("/deleteCourseDetails")
	public String deleteCourseDetails(@RequestParam("courseName") String courseName,
			RedirectAttributes redirectAttributes) {
		try {
			courseService.deleteCourseDetails(courseName);
			redirectAttributes.addFlashAttribute("successMsg", "Course Deleted Successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Course Not Deleted Successfully");
			e.printStackTrace();
		}
		return  "redirect:/courseManagement";

	}
	
	//--------------------admin feedback-------------------------------
	
	@GetMapping("/adminFeedback")
	public String openAdminFeedbackPage(Model model,@RequestParam(name="page",defaultValue="0")int page,
			                              @RequestParam(name="size",defaultValue="3")int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Feedback> feedbackPage = feedbackService.getAllFeedbackByPagenation(pageable);
		model.addAttribute("feedbackPage",feedbackPage);
		return "admin-feedback";
	}
	
	@PostMapping("/updateFeedbackStatus")
    public String updateFeedbackStatus(@RequestParam("id") Long id, @RequestParam("status") String status, RedirectAttributes redirectAttributes)
    {
        boolean success = feedbackService.updateFeedbackStatus(id, status);
        if (success) {
            redirectAttributes.addFlashAttribute("successMsg", "Feedback updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Failed to update feedback status.");
        }
        return "redirect:/adminFeedback"; // Redirect to the page where feedbacks are listed
    }

	
	@GetMapping("/adminLogout")
	public String adminLogout(SessionStatus sessionStatus)
	{
		sessionStatus.setComplete();
		return "admin-login";
	}

}
