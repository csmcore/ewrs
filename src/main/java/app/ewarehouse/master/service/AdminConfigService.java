package app.ewarehouse.master.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.entity.AdminConfiguration;
import jakarta.transaction.Transactional;


public interface AdminConfigService {
	
	public String saveConfigurationDetails(String adminConfiguration);

	List<AdminConfiguration> getAllConfigurationDetails();

	AdminConfiguration findConfigurationById(Integer configId);

	public Integer updateConfigurationDetails(Integer id, String updatedConfigData);

	public Page<AdminConfiguration> getAllConfigurationList(Pageable pageable);

	public long countFailedAttempt(String ip, String action, String status);

	public Integer countMaxAttempt();

	public String getLastFailedTime(String userId, String action, String status);

	public Map<String, Object> getNoOfAttemptAndIdleTimeValue();

	public long countBetweenLastAndCurrentTime(LocalDateTime failedDateTime, LocalDateTime now);

	public void updateLoginStatus(String userId, String action, String status);

	public Integer checkUserExist(String userId);

	public void blockUser(String userId);

	public void unBlockUser(String userId);

	public boolean isUserLocked(String userId);

	public String getEmailId(String userId);

	public String findUserNameById(Integer id);

}
