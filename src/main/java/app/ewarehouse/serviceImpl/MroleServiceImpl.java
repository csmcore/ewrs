package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.UserRoleIdResponse;
import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.MroleService;
import jakarta.persistence.EntityManager;

@Service
public class MroleServiceImpl implements MroleService {
	@Autowired
	private MroleRepository mroleRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	TuserRepository tuserRepository;
	@Autowired
	private ObjectMapper om;
	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(MroleServiceImpl.class);

	@Override
	//@CacheEvict(value = "dropdowns", allEntries = true)
	public JSONObject save(String data) {
		logger.info("Inside save method of MroleServiceImpl");
		JSONObject json = new JSONObject();
		int countForRole = 0;
		int countForAlias = 0;
		try {
			Mrole mrole = om.readValue(data, Mrole.class);
			mrole.setTxtRoleName(mrole.getTxtRoleName().trim());
			mrole.setTxtAliasName(mrole.getTxtAliasName().trim());
			if (!Objects.isNull(mrole.getIntId()) && mrole.getIntId() > 0) {

				countForRole = mroleRepository.getCountByRoleNameANDbitDeletedFlagNOTIntId(mrole.getIntId(),
						mrole.getTxtRoleName(), false);
				if (mrole.getTxtAliasName().length() > 0) {
					countForAlias = mroleRepository.getCountByAliasNameANDbitDeletedFlagNOTIntId(mrole.getIntId(),
							mrole.getTxtAliasName(), false);
				}
				if (countForRole > 0) {
					json.put("status", 401);
					return json;
				}
				if (countForAlias > 0) {
					json.put("status", 408);
					return json;
				}
				Mrole getEntity = mroleRepository.findByIntIdAndBitDeletedFlag(mrole.getIntId(), false);
				getEntity.setTxtRoleName(mrole.getTxtRoleName());
				getEntity.setTxtAliasName(mrole.getTxtAliasName());
				Mrole updateData = mroleRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				countForRole = mroleRepository.countByRoleNameANDBitDeletedFlag(mrole.getTxtRoleName(), false);
				if (mrole.getTxtAliasName().length() > 0) {
					countForAlias = mroleRepository.countByTxtAliasNameANDBitDeletedFlag(mrole.getTxtAliasName(),
							false);
				}
				if (countForRole < 1 && countForAlias < 1) {
					Mrole saveData = mroleRepository.save(mrole);
					parentId = saveData.getIntId();
					json.put("status", 200);
				} else {
					if (countForRole >= 1) {
						json.put("status", 401);
					} else if (countForAlias >= 1) {
						json.put("status", 408);
					}
				}
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of MroleServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of MroleServiceImpl");
		Mrole entity = mroleRepository.findByIntIdAndBitDeletedFlag(id, false);

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		logger.info("Inside getAll method of MroleServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Optional<String> searchValue = Optional.ofNullable(jsonData.get("searchValue").toString());

		List<Mrole> troleResp = null;
		if (!searchValue.isEmpty()) {
			troleResp = mroleRepository.findByRoleFilters(searchValue.get(), false);
		} else {
			troleResp = mroleRepository.findAllByBitDeletedFlag(false);
		}

		for (Mrole role : troleResp) {
			if (!(role.getTxtAliasName().length() > 0)) {
				role.setTxtAliasName("---");
			}
		}
		return new JSONArray(troleResp);
	}

	@Override
	@CacheEvict(value = "role", allEntries = true)
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of MroleServiceImpl");
		JSONObject json = new JSONObject();
		try {
			List<Tuser> user = tuserRepository.getByRoleIdandBitDeletedFlag(id, false);
			if (user.isEmpty()) {
				Mrole entity = mroleRepository.findByIntIdAndBitDeletedFlag(id, false);
				entity.setBitDeletedFlag(true);
				mroleRepository.save(entity);
				json.put("status", 200);
			} else {
				json.put("status", 300);

			}
		} catch (Exception e) {
			logger.error("Inside deleteById method of MroleServiceImpl some error occur:" + e);
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public List<UserRoleIdResponse> getUserByRoleId(int roleid) {

		List<UserRoleIdResponse> datalist = new ArrayList<UserRoleIdResponse>();
		List<Object[]> listobj = mroleRepository.getUserByRoleId(roleid);

		for (Object[] objects : listobj) {
			UserRoleIdResponse dataobj = new UserRoleIdResponse();
			dataobj.setUid(objects[0].toString());
			dataobj.setUname(objects[1].toString());
			dataobj.setLoginid(objects[2].toString());
			datalist.add(dataobj);
		}

		return datalist;
	}

}
