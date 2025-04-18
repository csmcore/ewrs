package app.ewarehouse.controller;

import app.ewarehouse.entity.ComplaintsCategory;
import app.ewarehouse.service.ComplaintsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaintscategories")
@CrossOrigin("*")
public class ComplaintsCategoryController {

	@Autowired
	private ComplaintsCategoryService complaintsCategoryService;
	
	@PostMapping
	public ResponseEntity<ComplaintsCategory> createComplaintCategory(@RequestBody ComplaintsCategory complaintsCategory) {
        ComplaintsCategory createdCategory = complaintsCategoryService.createComplaintCategory(complaintsCategory);
        return ResponseEntity.ok(createdCategory);
    }
	
	@GetMapping
    public ResponseEntity<List<ComplaintsCategory>> getAllComplaintCategories() {
        List<ComplaintsCategory> categories = complaintsCategoryService.getAllComplaintCategories();
        return ResponseEntity.ok(categories);
    }
	
	@GetMapping("/{categoryId}")
    public ResponseEntity<ComplaintsCategory> getComplaintCategoryById(@PathVariable Integer categoryId) {
        ComplaintsCategory category = complaintsCategoryService.getComplaintCategoryById(categoryId);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
