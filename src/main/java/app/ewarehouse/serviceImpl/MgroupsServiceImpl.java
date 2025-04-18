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

import app.ewarehouse.entity.Mgroups;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.MgroupsRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.MgroupsService;
import jakarta.persistence.EntityManager;
@Service
public class MgroupsServiceImpl implements MgroupsService {
	@Autowired
	private MgroupsRepository mgroupsRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private TuserRepository tuserRepository;
	@Autowired
    private ObjectMapper om;
	private static final Logger logger = LoggerFactory.getLogger(MgroupsServiceImpl.class);
	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject save(String data) {
		logger.info("Inside save method of MgroupsServiceImpl");
		JSONObject json = new JSONObject();
		int countForGroups=0;
		int countForAlias=0;
		try {
			Mgroups mgroups = om.readValue(data, Mgroups.class);
			mgroups.setTxtGroupName(mgroups.getTxtGroupName().trim());
			mgroups.setTxtAliasName(mgroups.getTxtAliasName().trim());
			if (!Objects.isNull(mgroups.getIntId()) && mgroups.getIntId() > 0) {
				
				countForGroups = mgroupsRepository.getCountByGroupNameANDbitDeletedFlagNOTIntId(mgroups.getIntId(),
						mgroups.getTxtGroupName(), false);
				if (mgroups.getTxtAliasName().length() > 0) {
					countForAlias = mgroupsRepository.getCountByAliasNameANDbitDeletedFlagNOTIntId(mgroups.getIntId(),
							mgroups.getTxtAliasName(), false);
				}
				if (countForGroups > 0) {
					json.put("status", 401);
					return json;
				}
				if (countForAlias > 0) {
					json.put("status", 408);
					return json;
				}
				
				Mgroups getEntity = mgroupsRepository.findByIntIdAndBitDeletedFlag(mgroups.getIntId(), false);
				getEntity.setTxtGroupName(mgroups.getTxtGroupName());
				getEntity.setTxtAliasName(mgroups.getTxtAliasName());
				Mgroups updateData = mgroupsRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				countForGroups = mgroupsRepository
						.countByGroupNameANDBitDeletedFlag(mgroups.getTxtGroupName(), false);
				if (mgroups.getTxtAliasName().length() > 0) {
					countForAlias = mgroupsRepository
							.countByTxtAliasNameANDBitDeletedFlag(mgroups.getTxtAliasName(), false);
				}
				if(countForGroups < 1 && countForAlias < 1) {
				Mgroups saveData = mgroupsRepository.save(mgroups);
				parentId = saveData.getIntId();
				json.put("status", 200);}
				else {
					if (countForGroups >= 1) {
						json.put("status", 401);
					} else if (countForAlias >= 1) {
						json.put("status", 408);
					}
					
				}
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of MgroupsServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of MgroupsServiceImpl");
		Mgroups entity = mgroupsRepository.findByIntIdAndBitDeletedFlag(id, false);

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		logger.info("Inside getAll method of MgroupsServiceImpl");
		List<Mgroups> tgroupsResp = mgroupsRepository.findAllByBitDeletedFlag(false);
		for (Mgroups groups : tgroupsResp) {
			if (!(groups.getTxtAliasName().length() > 0)) {
				groups.setTxtAliasName("---");
			}
		}
		return new JSONArray(tgroupsResp);
	}

	@Override
	@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of MgroupsServiceImpl");
		JSONObject json = new JSONObject();
		try {
			List<Tuser> user=tuserRepository.getByGroupsIdAndBitDeletedFlag(id, false);
			if(user.isEmpty()) {
			Mgroups entity = mgroupsRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			mgroupsRepository.save(entity);
			json.put("status", 200);}
			else {
				json.put("status", 300);
			}
		} catch (Exception e) {
			logger.error("Inside deleteById method of MgroupsServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

}
