package app.ewarehouse.serviceImpl;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.AuditTrailDto;
import app.ewarehouse.entity.AuditTrail;
import app.ewarehouse.repository.AuditTrailRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.AuditTrailService;
import app.ewarehouse.util.CommonUtil;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {

	private static final Logger logger = LoggerFactory.getLogger(AuditTrailServiceImpl.class);

	@Autowired
	private AuditTrailRepository auditTrailRepository;

	@Autowired
	private TuserRepository tuserRepo;

	@Autowired
	private MroleRepository mroleRepo;

	@Autowired
	private AuditTrailRepository auditTrailRepo;
	
	@Autowired
	ObjectMapper om;

	@Override
	public Page<AuditTrailDto> getAllAuditTrialList(Integer pageNumber, Integer pageSize, String sortCol,
			String sortDir, String search) {
		logger.info("Inside getAllAuditTrialList paginated method of AuditTrialServiceImpl");
		Page<Object> page;
		try {

			Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
					sortCol != null ? sortCol : "dtmCreatedOn");
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

			if (StringUtils.hasText(search)) {
				page = auditTrailRepository.findAuditTrialFilter(search, pageable);
			} else {
				page = auditTrailRepository.findAuditTrialDetails(pageable);
			}
		} catch (Exception e) {
			logger.info("Error occurred in getAllSubCountiesList paginated method of SubCountyServiceImpl: {}",e);
			throw new RuntimeException(e);
		}
		Page<AuditTrailDto> subCountyPage = page.map(obj -> mapToSubCounty((Object[]) obj));
		return subCountyPage;
	}

	private AuditTrailDto mapToSubCounty(Object[] obj) {
		AuditTrailDto dto = new AuditTrailDto();

		dto.setFirstname((String) obj[0]);
		dto.setRoleName((String) obj[1]);
		dto.setAction((String) obj[2]);
		dto.setRemarks((String) obj[3]);
		dto.setIpAddress((String) obj[4]);
		dto.setDeviceName((String) obj[5]);

		return dto;
	}

	@Override
	public String saveAuditTrail(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JSONObject jsonObject = new JSONObject(decodedData);
			int userId = jsonObject.getInt("userId");
			int roleId = jsonObject.getInt("roleId");
			String ipAddress = jsonObject.getString("ipAddress");
			double latitude = jsonObject.getDouble("latitude");
			double longitude = jsonObject.getDouble("longitude");
			String browserDetails = jsonObject.getString("browserDetails");
			String osDetails = jsonObject.getString("osDetails");
			String action = jsonObject.getString("action");
			String remarks = jsonObject.optString("remarks");
			Runnable auditTrailRunnable = () -> {
				AuditTrail ad = new AuditTrail();
				if (userId != 0) {
					ad.setUser(tuserRepo.findByIntIdAndBitDeletedFlag(userId, false));
				} else {
					ad.setUser(null);
				}
				if (roleId != 0) {
					ad.setRole(mroleRepo.findByIntIdAndBitDeletedFlag(roleId, false));
				} else {
					ad.setRole(null);
				}
				ad.setIpAddress(ipAddress);
				ad.setLatitude(Double.toString(latitude));
				ad.setLongitude(Double.toString(longitude));
				ad.setDeviceName(browserDetails);
				ad.setDtmCreatedOn(LocalDateTime.now());
				ad.setOs(osDetails);
				ad.setAction(action);
				ad.setRemarks(org.apache.tika.utils.StringUtils.isBlank(remarks) ? null : remarks);
				auditTrailRepo.saveAndFlush(ad);
			};
			new Thread(auditTrailRunnable).start();

			json.put("status", 200);
		} catch (Exception e) {
			logger.error("AuditTrailServiceImpl:saveAuditTrail()",e);
			json.put("status", 500);
		}
		return json.toString();
	}

}
