package app.ewarehouse.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import app.ewarehouse.entity.ComplaintsCategory;
import app.ewarehouse.repository.ComplaintsCategoryRepository;
import app.ewarehouse.service.ComplaintsCategoryService;

@Service
public class ComplaintsCategoryServiceImpl implements ComplaintsCategoryService {
	
	@Autowired
	private  ComplaintsCategoryRepository  complaintsCategoryRepository;

	@Override
	public ComplaintsCategory createComplaintCategory(ComplaintsCategory complaintsCategory) {
		 return complaintsCategoryRepository.save(complaintsCategory);
	}

	@Override
	public ComplaintsCategory getComplaintCategoryById(Integer categoryId) {
		java.util.Optional<ComplaintsCategory> optionalCategory = complaintsCategoryRepository.findById(categoryId);
        return optionalCategory.orElse(null);
	}

	@Override
	public List<ComplaintsCategory> getAllComplaintCategories() {
		return complaintsCategoryRepository.findAll();
	}
}

