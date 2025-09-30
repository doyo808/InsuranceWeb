package com.kd.insuranceweb.helpdesk.service;

import java.util.List;

import com.kd.insuranceweb.helpdesk.dto.FaqDto;



public interface FaqService {
	
	List<FaqDto> getAllFaqs();
	
	List<FaqDto> getFaqsByCategory(String category);
	
	List<FaqDto> searchFaqs(String keyword);
	
	

}
