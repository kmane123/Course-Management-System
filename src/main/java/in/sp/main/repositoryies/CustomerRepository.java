package in.sp.main.repositoryies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import in.sp.main.entities.User;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<User, Long>
{
      User findByEmail(String email);
}
