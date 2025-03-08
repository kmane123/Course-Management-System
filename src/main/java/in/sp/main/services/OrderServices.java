package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Orders;
import in.sp.main.repositoryies.OrdersRepository;

@Service
public class OrderServices 
{
	  @Autowired
	  private OrdersRepository orderRepository;
	 
      public void storeUserOrders(Orders orders)
      {
    	  orderRepository.save(orders);
      }
}
