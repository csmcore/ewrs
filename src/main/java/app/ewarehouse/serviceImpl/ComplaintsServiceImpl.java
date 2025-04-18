package app.ewarehouse.serviceImpl;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.ComplaintsResponse;
import app.ewarehouse.entity.ComplaintObservation;
import app.ewarehouse.entity.Complaints;
import app.ewarehouse.entity.Status;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.ComplaintObservationRepository;
import app.ewarehouse.repository.ComplaintsRepository;
import app.ewarehouse.service.ComplaintsService;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.Mapper;

@Service
public class ComplaintsServiceImpl implements ComplaintsService {

	@Autowired
	private ComplaintsRepository complaintsRepository;
	@Autowired
	ComplaintObservationRepository  observationRepo;
	@Override
	public Complaints save(Complaints complaints) {
		try {

			byte[] decode = Base64.getDecoder().decode(complaints.getSupportingDocument().getBytes());
			String uniqueFileName = "Complaint_Report_" + UUID.randomUUID().toString();
			String file_url = DocumentUploadutil.uploadFileByte(uniqueFileName, decode,
					FolderAndDirectoryConstant.COMPLAINT_REPORTING_FOLDER);
			
			if (file_url.startsWith("Document")) {
				throw new RuntimeException(file_url);
			}

			String filePath = file_url.substring(file_url.indexOf(FolderAndDirectoryConstant.COMPLAINT_REPORTING_FOLDER));
			System.out.println(filePath);
			complaints.setSupportingDocument(filePath);
	
			complaints.setStatus(Status.Pending);
			complaintsRepository.save(complaints);
			return complaints;
		} catch (Exception e) {
			throw  new CustomGeneralException("Complaint not saved");
		}
	}

	@Override
	public List<ComplaintsResponse> getAll() {
		return complaintsRepository.findAll().stream().map(Mapper::mapToComplaintsResponse).toList();

//		return complaintsRepository.findByBitDeleteFlag(false).stream().map(Mapper::mapToComplaintsResponse).toList();

	}

	@Override
	public Complaints getById(String id) {
		java.util.Optional<Complaints> optionalComplaints = complaintsRepository.findById(id);
		return optionalComplaints.orElse(null);
	}

	@Override
	public Complaints update(String id, Complaints updatedComplaints) {
		try {
			if (complaintsRepository.existsById(id)) {
				updatedComplaints.setComplaintId(id);
				return complaintsRepository.save(updatedComplaints);
			}else{
				throw new CustomGeneralException("Complaint with ID " + id + " not found");
			}
		}catch (Exception e){
			throw  new CustomGeneralException("Complaint not Updated");
		}
    }

	@Override
	public boolean deleteById(Integer id) {
		if (complaintsRepository.existsById(String.valueOf(id))) {
			complaintsRepository.deleteById(String.valueOf(id));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ComplaintObservation> getComplaintOservation(Integer lableId,Integer RoleId) {
		
		return observationRepo.findByIntLableIdAndIntRoleId(lableId, RoleId);
	}

}
