package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.repositoryies.EmpSalesInfoRepository;
import in.sp.main.repositoryies.EmployeeOrderRepository;

@Service
public class EmpSalesInfoService 
{
  @Autowired
  private EmpSalesInfoRepository empSalesInfoRepository;
  
  public String findTotalSalesByAllEmployees()
  {
	  return empSalesInfoRepository.findTotalSalesByAllEmployees();
  }
  
  public List<Object[]> findTotalSalesByEachEmployee()
  {
	  return empSalesInfoRepository.findTotalSalesByEachEmployee();
  }

}
