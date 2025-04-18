package app.ewarehouse.master.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.AdminConfiguration;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.repository.AdminConfigRepository;
import app.ewarehouse.master.service.AdminConfigService;
import app.ewarehouse.util.CommonUtil;
import jakarta.transaction.Transactional;

@Service
public class AdminConfigServiceImpl implements AdminConfigService {
	
	@Autowired
    private AdminConfigRepository adminConfigRepository;
	private static final Logger logger = LoggerFactory.getLogger(AdminConfigServiceImpl.class);

	public String saveConfigurationDetails(String adminConfiguration) {
	    AdminConfiguration adminConfig = null; 
	    try {
	        String decodedData = CommonUtil.inputStreamDecoder(adminConfiguration);
	        
	        try {
	            adminConfig = new ObjectMapper().readValue(decodedData, AdminConfiguration.class);
	        } catch (Exception e) {
	            throw new CustomGeneralException("Invalid data format: " +e);
	        }
	        
	        AdminConfiguration saveData = adminConfigRepository.save(adminConfig);
	        return saveData.getUserName();
	    } finally {
	        if (adminConfig != null) {
	        	logger.info("Processed admin configuration for user: "+adminConfig.getUserName());
	        } else {
	        	logger.info("No valid admin configuration processed.");
	        }
	    }
	}



	@Override
	public List<AdminConfiguration> getAllConfigurationDetails() {
		return adminConfigRepository.findAll();
	}

	@Override
	public AdminConfiguration findConfigurationById(Integer configId) {
		 Optional<AdminConfiguration> configData = adminConfigRepository.findById(configId);
        return configData.orElse(null);
	}


	@Transactional
	@Override
	public Integer updateConfigurationDetails(Integer configId, String updatedConfigData) {
	    try {
	        String decodedData = CommonUtil.inputStreamDecoder(updatedConfigData);

	        AdminConfiguration adminConfiguration = new ObjectMapper().readValue(decodedData, AdminConfiguration.class);

	        Optional<AdminConfiguration> existingConfigurationOpt = adminConfigRepository.findById(configId);

	        if (existingConfigurationOpt.isPresent()) {
	            AdminConfiguration existingConfig = existingConfigurationOpt.get();
	            existingConfig.setUserName(adminConfiguration.getUserName());
	            existingConfig.setNoOfAttempt(adminConfiguration.getNoOfAttempt());
	            existingConfig.setIdleTime(adminConfiguration.getIdleTime());

	            adminConfigRepository.save(existingConfig);

	            return 1; 
	        } else {
	            return 0; 
	        }
	    } catch (Exception e) {
	        logger.error("Error updating configuration:"+e);
	        throw new RuntimeException("Error updating configuration: "+e); 
	    }
	}

	@Override
	public Page<AdminConfiguration> getAllConfigurationList(Pageable pageable) {
		return adminConfigRepository.findAllByOrderByDtmCreatedAtDesc(pageable);
	}



	@Override
	public long countFailedAttempt(String ip, String action, String status) {
		return adminConfigRepository.count(ip,action,status);
	}



	@Override
	public Integer countMaxAttempt() {
		return adminConfigRepository.getMaxAttempt();
	}



	@Override
	public String getLastFailedTime(String userId, String action, String status) {
		return adminConfigRepository.getLastFailedTime(userId,action,status);
	}



	@Override
	public Map<String, Object> getNoOfAttemptAndIdleTimeValue() {
		return adminConfigRepository.getNoOfAttemptAndIdleTimeValue();
	}



	@Override
	public long countBetweenLastAndCurrentTime(LocalDateTime failedDateTime, LocalDateTime now) {
		return adminConfigRepository.countBetweenLastAndCurrentTime(failedDateTime,now);
	}


	@Transactional
	@Override
	public void updateLoginStatus(String userId, String action, String status) {
		adminConfigRepository.updateLoginStatus(userId,action,status);
	}



	@Override
	public Integer checkUserExist(String userId) {
		return adminConfigRepository.checkUserExist(userId);
	}


	@Transactional
	@Override
	public void blockUser(String userId) {
		adminConfigRepository.blockUser(userId);		
	}


	@Transactional
	@Override
	public void unBlockUser(String userId) {
		adminConfigRepository.unBlockUser(userId);
	}



	@Override
	public boolean isUserLocked(String userId) {
		return adminConfigRepository.isUserLocked(userId);
	}
	
	@Override
	public String getEmailId(String userId) {
		return adminConfigRepository.getEmailId(userId);
	}

	@Override
	public String findUserNameById(Integer id) {
		return adminConfigRepository.findUserNameById(id);
	}

}
