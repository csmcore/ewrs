package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.entity.ComplaintsCategory;

public interface ComplaintsCategoryService {

    ComplaintsCategory createComplaintCategory(ComplaintsCategory complaintsCategory);

    ComplaintsCategory getComplaintCategoryById(Integer categoryId);

    List<ComplaintsCategory> getAllComplaintCategories();
}

//boolean save(ComplaintsCategory complaintCategory);
//
//List<ComplaintsCategory> getAll();
//
// ComplaintsCategory getById(Integer id);
//}