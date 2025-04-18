package app.ewarehouse.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.LoginTrailDTO;
import app.ewarehouse.dto.LoginTrailRequestDTO;
import app.ewarehouse.entity.LoginAttemptStatus;
import app.ewarehouse.entity.LoginTrail;
import app.ewarehouse.repository.LoginTrailRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.LoginTrailService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
@Service
public class LoginTrailServiceImpl implements LoginTrailService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginTrailServiceImpl.class);
	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	
	private LoginTrailRepository loginTrailRepository;
	private TuserRepository tuserRepo;
	private MroleRepository mroleRepo;
	private final ObjectMapper om;

	public LoginTrailServiceImpl(LoginTrailRepository loginTrailRepository,TuserRepository tuserRepo,MroleRepository mroleRepo,ObjectMapper om) {
		this.loginTrailRepository = loginTrailRepository;
		this.tuserRepo = tuserRepo;
		this.mroleRepo = mroleRepo;
		this.om = om;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LoginTrailDTO> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir,
			String search) {	
		
		    logger.info("Inside getAll paginated method of LoginTrailServiceImpl");
	        try {

	            Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
	                    sortCol != null ? sortCol : "loginTrailId");
	            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

	            Page<LoginTrail> page;
	            if (StringUtils.hasText(search)) {
	                page = loginTrailRepository.findLoginTrailByFiltes(search, pageable);
	            } else {
	                page = loginTrailRepository.findAllLoginTrail(pageable);
	            }

	            List<LoginTrailDTO> responses = page.getContent()
	                    .stream()
	                    .map(Mapper::mapToLoginTrailDto)
	                    .toList();
	            return new PageImpl<>(responses, pageable, page.getTotalElements());
	        } catch (Exception e) {
	            logger.info("Error occurred in getAll paginated method of LoginTrailServiceImpl: {}", e.getMessage(), e);
	            throw new RuntimeException(e);
	        }
	}

	@Override
	public JSONObject saveLoginTrailData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			LoginTrailRequestDTO obj = om.readValue(decodedData, LoginTrailRequestDTO.class);
			Runnable loginTrailRunnable = ()->{
				LoginTrail entity = new LoginTrail();
				if(obj.getUserId() != null) {
					entity.setUser(tuserRepo.findByIntIdAndBitDeletedFlag(obj.getUserId(), false));
				}else {
				entity.setUser(null);
				}
				if(obj.getRoleId() != null) {
					entity.setRole(mroleRepo.findByIntIdAndBitDeletedFlag(obj.getRoleId(), false));
				}else {
				entity.setRole(null);
				}
				entity.setIpAddress(obj.getIpAddress());
				entity.setLatitude(obj.getLatitude());
				entity.setLongitude(obj.getLongitude());
				entity.setOs(obj.getBrowserDetails().getPlatform());
				entity.setDateTime(LocalDateTime.now());
				entity.setDeviceName(obj.getBrowserDetails().getBrowser());
				entity.setAction(obj.getAction());
				entity.setBitLoginAttempted(obj.getEnmStatus().equalsIgnoreCase("FAILURE"));
				if("SUCCESS".equals(obj.getEnmStatus())) {
				entity.setEnmStatus(LoginAttemptStatus.SUCCESS);
				}else if("FAILURE".equals(obj.getEnmStatus())) {
					entity.setEnmStatus(LoginAttemptStatus.FAILURE);
				}
				entity.setVchEnteredUserName(obj.getEnteredUserName());
				loginTrailRepository.save(entity);
			};
			new Thread(loginTrailRunnable).start();
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}
	
	
	
	

}
