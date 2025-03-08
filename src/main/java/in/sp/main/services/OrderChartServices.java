package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.repositoryies.OrderChartRepository;

@Service
public class OrderChartServices 
{
   @Autowired
   private OrderChartRepository orderChartRepository;
   
   public List<Object[]> findCoursesAmountTotalSales()
   {
	   return orderChartRepository.findCoursesAmountTotalSales();
   }
   
   public List<Object[]> findCoursesTotalSales()
   {
	   return orderChartRepository.findCoursesTotalSales();
   }
   
   public List<Object[]> findCourseSoldPerDay()
   {
	   return orderChartRepository.findCoursesSoldPerDay();
   }
}
