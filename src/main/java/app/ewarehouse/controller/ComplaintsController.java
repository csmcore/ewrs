package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.ewarehouse.dto.ComplaintsResponse;
import app.ewarehouse.entity.Complaints;
import app.ewarehouse.service.ComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin("*")
public class ComplaintsController {
	
	@Autowired
	private ComplaintsService complaintsService;
	
	@PostMapping
	public ResponseEntity<?> createComplaint(@RequestBody Complaints complaints) {
	   Complaints complaints1 = complaintsService.save(complaints);
        return new ResponseEntity<>(complaints, HttpStatus.OK);

	}

    @GetMapping
    public ResponseEntity<List<ComplaintsResponse>> getAllComplaints() {
        List<ComplaintsResponse> complaints = complaintsService.getAll();
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaints> getComplaintById(@PathVariable String id) {
        Complaints complaints = complaintsService.getById(id);
        if (complaints != null) {
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComplaint(@PathVariable String id, @RequestBody Complaints updatedComplaints) {
         Complaints complaints = complaintsService.update(id, updatedComplaints);
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComplaint(@PathVariable Integer id) {
        boolean isDeleted = complaintsService.deleteById(id);
        if (isDeleted) {
            return new ResponseEntity<>("Complaint deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete complaint", HttpStatus.NOT_FOUND);
        }
    }
}

