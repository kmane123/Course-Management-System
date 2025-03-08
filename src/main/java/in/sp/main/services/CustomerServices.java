package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.User;
import in.sp.main.repositoryies.CustomerRepository;

@Service
public class CustomerServices 
{

	@Autowired
	private CustomerRepository customerRepository;
	
	public Page<User> getAllUserDetailsByPageable(Pageable pageable)
	{
		return customerRepository.findAll(pageable);
	}
	
	public User getAllDetailsByEmail(String email)
	{
		return customerRepository.findByEmail(email);
	}
	
	public void updateUserBanStatus(User user)

	{
		customerRepository.save(user);
	}
}
