package in.sp.main.repositoryies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Employee;
import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> 
{
     Employee findByName(String employeeName);
     Employee findByEmail(String email);

}
