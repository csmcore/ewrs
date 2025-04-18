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

import app.ewarehouse.entity.Mdepartment;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.MdepartmentRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.MdepartmentService;
import jakarta.persistence.EntityManager;
@Service
public class MdepartmentServiceImpl implements MdepartmentService {
	@Autowired
	private MdepartmentRepository mdepartmentRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TuserRepository tuserRepository;
	@Autowired
    private ObjectMapper om;

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(MdepartmentServiceImpl.class);

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject save(String data) {
		logger.info("Inside save method of MdepartmentServiceImpl");
		JSONObject json = new JSONObject();
		int countForDept = 0;
		int countForAlias = 0;
		try {
			Mdepartment mdepartment = om.readValue(data, Mdepartment.class);
			mdepartment.setTxtDepartmentName(mdepartment.getTxtDepartmentName().trim());
			mdepartment.setTxtAliasName(mdepartment.getTxtAliasName().trim());
			if (!Objects.isNull(mdepartment.getIntId()) && mdepartment.getIntId() > 0) {
				countForDept = mdepartmentRepository.getCountByDeptName(mdepartment.getIntId(),
						mdepartment.getTxtDepartmentName(), false);
				if (mdepartment.getTxtAliasName().length() > 0) {
					countForAlias = mdepartmentRepository.getCountByAliasName(mdepartment.getIntId(),
							mdepartment.getTxtAliasName(), false);
				}
				if (countForDept > 0) {
					json.put("status", 401);
					return json;
				}
				if (countForAlias > 0) {
					json.put("status", 408);
					return json;
				}

				Mdepartment getEntity = mdepartmentRepository.findByIntIdAndBitDeletedFlag(mdepartment.getIntId(),
						false);
				getEntity.setTxtDepartmentName(mdepartment.getTxtDepartmentName());
				getEntity.setTxtAliasName(mdepartment.getTxtAliasName());
				Mdepartment updateData = mdepartmentRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);

			} else {

				countForDept = mdepartmentRepository
						.countByTxtDepartmentNameANDBitDeletedFlag(mdepartment.getTxtDepartmentName(), false);
				if (mdepartment.getTxtAliasName().length() > 0) {
					countForAlias = mdepartmentRepository
							.countByTxtAliasNameANDBitDeletedFlag(mdepartment.getTxtAliasName(), false);
				}

				if (countForDept < 1 && countForAlias < 1) {
					Mdepartment saveData = mdepartmentRepository.save(mdepartment);
					parentId = saveData.getIntId();
					json.put("status", 200);
				} else {
					if (countForDept >= 1) {
						json.put("status", 401);
					} else if (countForAlias >= 1) {
						json.put("status", 408);
					}
				}

			}

			json.put("id", parentId);

		} catch (Exception e) {
			logger.error("Inside save method of MdepartmentServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of MdepartmentServiceImpl");
		Mdepartment entity = mdepartmentRepository.findByIntIdAndBitDeletedFlag(id, false);
		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		logger.info("Inside getAll method of MdepartmentServiceImpl");
		
		List<Mdepartment> tdepartmentResp = mdepartmentRepository.findAllByBitDeletedFlag(false);
		for (Mdepartment mdepartment : tdepartmentResp) {
			if (!(mdepartment.getTxtAliasName().length() > 0)) {
				mdepartment.setTxtAliasName("---");
			}
		}
		
		return new JSONArray(tdepartmentResp);
	}

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of FeedbackformServiceImpl");
		JSONObject json = new JSONObject();
		try {
			List<Tuser> user = tuserRepository.getBydeptIdandbitDeletFlag(id, false);

			if (user.isEmpty()) {
				Mdepartment entity = mdepartmentRepository.findByIntIdAndBitDeletedFlag(id, false);
				entity.setBitDeletedFlag(true);
				mdepartmentRepository.save(entity);
				json.put("status", 200);
			} else {
				json.put("status", 300);
			}
		} catch (Exception e) {
			logger.error("Inside deleteById method of MdepartmentServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

}
