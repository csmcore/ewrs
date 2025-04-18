/**
 * 
 */
package app.ewarehouse.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ComplaintTypeResponse;
import app.ewarehouse.entity.ComplaintType;
import app.ewarehouse.entity.County;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.ComplaintTypeRepository;
import app.ewarehouse.service.MasterService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityNotFoundException;

/**
 * Priyanka Singh
 */
@Service
public class MasterServiceImpl implements MasterService {

	private static final Logger logger = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	private ComplaintTypeRepository complaintTypeRepository;
	
	@Autowired
    private ObjectMapper om;

	public Integer saveComplainttype(String complaintType) {
		String decodedData = CommonUtil.inputStreamDecoder((complaintType));
		ComplaintType type;
		try {
			type = om.readValue(decodedData, ComplaintType.class);
			ComplaintType ComplaintTypeDetails = complaintTypeRepository
					.findByComplaintTypeIgnoreCase(type.getComplaintType());
			if (ComplaintTypeDetails == null) {
				Calendar calendar = Calendar.getInstance();
				type.setCreatedOn((calendar.getTime()));
				type.setCreatedBy("ok");
				type.setIsActive(false);
				type = complaintTypeRepository.save(type);
				if (type != null) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return 2;
			}
		} catch (Exception e) {
			logger.info("Inside saveComplainttype method of MasterServiceImpl");
			return 0;
		}
	}



	public Integer update(String complaintType) {
		logger.info("Inside update method of MasterServiceImpl");
		String decodedData = CommonUtil.inputStreamDecoder((complaintType));
		ComplaintType type;
		try {
			type = om.readValue(decodedData, ComplaintType.class);
			ComplaintType duplicateComplaintType = complaintTypeRepository
					.findByComplaintTypeIgnoreCase(type.getComplaintType());
			ComplaintType existingComplaintType = complaintTypeRepository
					.findByComplaintId(type.getComplaintId());
			if (existingComplaintType == null) {
				return 0; 
			}
			if (duplicateComplaintType != null
					&& !duplicateComplaintType.getComplaintId().equals(type.getComplaintId())) {
				return 2; 
			}
			Calendar calendar = Calendar.getInstance();
			existingComplaintType.setComplaintType(type.getComplaintType());
			existingComplaintType.setComplaintStatus(type.getComplaintStatus());
			existingComplaintType.setUpdatedBy(type.getUpdatedBy());
			existingComplaintType.setUpdatedOn(calendar.getTime());
			complaintTypeRepository.save(existingComplaintType);
			return 1; 
		} catch (Exception e) {
			logger.info("Inside update method of MasterServiceImpl");
			return 0; 
		}
	}

	@Override
	public ComplaintType getbyid(Integer complaintId) {
			return complaintTypeRepository.findById(complaintId).get();
	}


	@Override
	public ComplaintTypeResponse getById(Integer complaintId) {
		logger.info("Inside getById method of MasterServiceImpl");
		ComplaintType complainttype = complaintTypeRepository.findByComplaintId(complaintId);
		return Mapper.mapToComplaintTypeResponse(complainttype);
	}


	@Override
    public Integer resetComplaintId(Integer complaintId) {
		logger.info("Inside resetComplaintId method of MasterServiceImpl");
        try {
            if (complaintId != null) {
                int updatedRows = complaintTypeRepository.resetComplaintId(complaintId);
                return updatedRows > 0 ? 1 : 0;
            }
            return 0;
        } catch (Exception e) {
        	logger.info("Inside resetComplaintId method of MasterServiceImpl");
            return 0;
        }
    }



	@Override
	public Page<ComplaintTypeResponse> getAllcomplaintList(Pageable pageable) {
			return complaintTypeRepository.findAllData(pageable)
					.map(Mapper::mapToComplaintTypeResponse);
	}

	@Override
	public void deleteComplaint(Integer complaintId) {
			ComplaintType entity = complaintTypeRepository.findById(complaintId).orElseThrow(() -> new CustomGeneralException("Entity not found"));
			entity.setIsActive(!entity.getIsActive());
			complaintTypeRepository.save(entity);
	}

	@Override
	public List<ComplaintTypeResponse> getAllComplaintTypes() {
			return complaintTypeRepository.findByIsActive(false).stream()
					.map(Mapper::mapToComplaintTypeResponse)
					.toList();
	}



	@Override
	public List<ComplaintTypeResponse> getComplaintTypesByRoleId(Integer roleId) {
		return complaintTypeRepository.getComplaintTypesByRoleId(roleId).stream()
				.map(Mapper::mapToComplaintTypeResponse)
				.toList();
	}



	@Override
	@Transactional(readOnly = true)
	public Page<ComplaintTypeResponse> getAllcomplaintList(Integer pageNumber, Integer pageSize, String search) {
		logger.info("Inside getAllcomplaintList paginated method of MasterServiceImpl");
        try {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<ComplaintType> page;
            if (StringUtils.hasText(search)) {
                page = complaintTypeRepository.findByFilters(search, pageable);
            } else {
                page = complaintTypeRepository.findAllComplaintList(pageable);
            }

            List<ComplaintTypeResponse> countryResponses = page.getContent()
                    .stream()
                    .map(Mapper::mapToComplaintTypeResponse)
                    .toList();
            return new PageImpl<>(countryResponses, pageable, page.getTotalElements());
        } catch (Exception e) {
            logger.info("Error occurred in getAll paginated method of CountryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
	}



	 @Override
	 @Transactional
	 //@CacheEvict(value = "getCounties", allEntries = true)
	public void deleteComplaintType(Integer id) {
		 if (!complaintTypeRepository.existsById(id)) {
		    throw new EntityNotFoundException("Complaint type with id " + id + " not found");
		 }
		 ComplaintType complaint = complaintTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Complaint type with id " + id + " not found"));
		 complaint.setIsActive(!complaint.getIsActive());
		  complaintTypeRepository.save(complaint);
	   }
		
}
