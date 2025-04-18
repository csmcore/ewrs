package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.Mdesignation;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.MdesignationRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.MdesignationService;
import jakarta.persistence.EntityManager;
@Service
public class MdesignationServiceImpl implements MdesignationService {
	@Autowired
	private MdesignationRepository mdesignationRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TuserRepository tuserRepository;
	@Autowired
    private ObjectMapper om;
	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(MdesignationServiceImpl.class);

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		int countForDesg = 0;
		int countForAlias = 0;
		logger.info("Inside save method of MdesignationServiceImpl");
		try {
			Mdesignation mdesignation = om.readValue(data, Mdesignation.class);
			mdesignation.setTxtDesignationName(mdesignation.getTxtDesignationName().trim());
			mdesignation.setTxtAliasName(mdesignation.getTxtAliasName().trim());
			if (!Objects.isNull(mdesignation.getIntId()) && mdesignation.getIntId() > 0) {
				countForDesg = mdesignationRepository.getCountByDesgNameANDbitDeletedFlagNOTIntId(mdesignation.getIntId(),
						mdesignation.getTxtDesignationName(), false);
				if (mdesignation.getTxtAliasName().length() > 0) {
					countForAlias = mdesignationRepository.getCountByAliasNameANDbitDeletedFlagNOTIntId(mdesignation.getIntId(),
							mdesignation.getTxtAliasName(), false);
				}
				if (countForDesg > 0) {
					json.put("status", 401);
					return json;
				}
				if (countForAlias > 0) {
					json.put("status", 408);
					return json;
				}
				Mdesignation getEntity = mdesignationRepository.findByIntIdAndBitDeletedFlag(mdesignation.getIntId(),
						false);
				getEntity.setTxtDesignationName(mdesignation.getTxtDesignationName());
				getEntity.setTxtAliasName(mdesignation.getTxtAliasName());
				Mdesignation updateData = mdesignationRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				
				countForDesg = mdesignationRepository
					.countByTxtDepartmentNameANDBitDeletedFlag(mdesignation.getTxtDesignationName(), false);
			if (mdesignation.getTxtAliasName().length() > 0) {
				countForAlias = mdesignationRepository
						.countByTxtAliasNameANDBitDeletedFlag(mdesignation.getTxtAliasName(), false);
			}
				
				if(countForDesg < 1 && countForAlias < 1) {
				Mdesignation saveData = mdesignationRepository.save(mdesignation);
				parentId = saveData.getIntId();
				json.put("status", 200);
				}else {
					if (countForDesg >= 1) {
						json.put("status", 401);
					} else if (countForAlias >= 1) {
						json.put("status", 408);
					}
					
				}
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of MdesignationServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of MdesignationServiceImpl");
		Mdesignation entity = mdesignationRepository.findByIntIdAndBitDeletedFlag(id, false);
		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		logger.info("Inside getAll method of MdesignationServiceImpl");
		List<Mdesignation> tdesignationResp = mdesignationRepository.findAllByBitDeletedFlag(false);
		for (Mdesignation mdesignation : tdesignationResp) {
			if (!(mdesignation.getTxtAliasName().length() > 0)) {
				mdesignation.setTxtAliasName("---");
			}
		}
		return new JSONArray(tdesignationResp);
	}

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		logger.info("Inside deleteById method of MdesignationServiceImpl");
		try {
			List<Tuser> user=tuserRepository.getByDesignationIdandBitDeletedFlag(id, false);
			if(user.isEmpty()) {
			Mdesignation entity = mdesignationRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			mdesignationRepository.save(entity);
			json.put("status", 200);}
			else {
				json.put("status", 300);
			}
		} catch (Exception e) {
			logger.error("Inside deleteById method of MdesignationServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

}
