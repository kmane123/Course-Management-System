package in.sp.main.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Feedback;
import in.sp.main.repositoryies.FeedbackRepository;

@Service
public class FeedbackService 
{
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	public void sendFeedback(Feedback feedback)
	{
		feedbackRepository.save(feedback);
	}
	
	public Page<Feedback> getAllFeedbackByPagenation(Pageable pagable)
	{
		return feedbackRepository.findAll(pagable);
	}
	
	public boolean updateFeedbackStatus(Long id, String status)
	{
       
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        
        if (feedback != null) 
        {
            feedback.setReadStatus(status);
            feedbackRepository.save(feedback);
            return true; 
        }
        
        return false;
    }


}
