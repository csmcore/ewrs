package app.ewarehouse.master.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import app.ewarehouse.dto.CountryResponse;
import app.ewarehouse.entity.Country;
import app.ewarehouse.exception.DuplicateComplaintStatusException;
import app.ewarehouse.master.dto.ComplaintStatusMasterDto;
import app.ewarehouse.master.dto.ComplaintStatusResponseDto;
import app.ewarehouse.master.entity.ComplaintStatusMaster;
import app.ewarehouse.master.repository.ComplaintStatusMasterRepository;
import app.ewarehouse.master.service.ComplaintStatusMasterService;
import app.ewarehouse.util.Mapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinmaya.jena
 * @since 03-07-2024
 */

@Service
public class ComplaintStatusMasterServiceImpl implements ComplaintStatusMasterService {

	private static final Logger logger = LoggerFactory.getLogger(ComplaintStatusMasterServiceImpl.class);

    private final ComplaintStatusMasterRepository repository;

    public ComplaintStatusMasterServiceImpl(ComplaintStatusMasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public ComplaintStatusMaster createComplaintStatus(ComplaintStatusMasterDto complaintStatusMasterDto) {
        logger.info("Creating new complaint status: {}", complaintStatusMasterDto.getVchComplaintStatusName());
        ComplaintStatusMaster complaintStatusMaster = new ComplaintStatusMaster();
        complaintStatusMaster.setVchComplaintStatusName(complaintStatusMasterDto.getVchComplaintStatusName());
        try {
            return repository.save(complaintStatusMaster);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateComplaintStatusException("Complaint status name already exists: " + complaintStatusMasterDto.getVchComplaintStatusName());
        }
    }

    @Override
    public ComplaintStatusMaster updateComplaintStatus(Integer id, ComplaintStatusMasterDto complaintStatusMasterDto) {
        logger.info("Updating complaint status with ID {}: {}", id, complaintStatusMasterDto.getVchComplaintStatusName());
        Optional<ComplaintStatusMaster> optionalComplaintStatusMaster = repository.findById(id);
        if (optionalComplaintStatusMaster.isPresent()) {
            ComplaintStatusMaster complaintStatusMaster = optionalComplaintStatusMaster.get();
            complaintStatusMaster.setVchComplaintStatusName(complaintStatusMasterDto.getVchComplaintStatusName());
            return repository.save(complaintStatusMaster);
        }
        logger.warn("Complaint status with ID {} not found for update.", id);
        return null;
    }

    @Override
    public ComplaintStatusMaster getComplaintStatusById(Integer id) {
        logger.info("Fetching complaint status with ID {}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ComplaintStatusMaster> getAllComplaintStatuses() {
        logger.info("Fetching all complaint statuses");
        return repository.findAll();
    }

    @Transactional
    @Override
    public JSONObject changeComplaintStatus(Integer id) {
    	JSONObject json = new JSONObject();
    	try {
        logger.info("Changing status for complaint with ID {}", id);
        repository.changeBitDeletedFlag(id);
        ComplaintStatusMaster data = repository.findById(id).orElse(null);
        json.put("status", 200);
        json.put("isDeletedStatus", data.getBitDeletedFlag());
    	}catch(Exception e) {
    		json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    		json.put("message",e.getMessage());
    	}
        return json;
    }

	@Override
	@Transactional(readOnly = true)
	public Page<ComplaintStatusResponseDto> getAll(Integer pageNumber, Integer pageSize, String search) {
		logger.info("Inside getAll paginated method of ComplaintStatusMasterServiceImpl");
        try {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<ComplaintStatusMaster> page;
            if (StringUtils.hasText(search)) {
                page = repository.findByFilters(search, pageable);
            } else {
                page = repository.findAllComplaintStatus(pageable);
            }

            List<ComplaintStatusResponseDto> responses = page.getContent()
                    .stream()
                    .map(Mapper::mapToComplaintStatusDto)
                    .toList();
            return new PageImpl<>(responses, pageable, page.getTotalElements());
        } catch (Exception e) {
            logger.info("Error occurred in getAll paginated method of ComplaintStatusMasterServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
	}
}
