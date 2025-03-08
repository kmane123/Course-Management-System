package in.sp.main.repositoryies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Course;
import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
         Course findByName(String courseName);
}
