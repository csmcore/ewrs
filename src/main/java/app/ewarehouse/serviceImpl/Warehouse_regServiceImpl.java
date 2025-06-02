package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.Warehouse_reg;
import app.ewarehouse.repository.Warehouse_regRepository;
import app.ewarehouse.service.Warehouse_regService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class Warehouse_regServiceImpl implements Warehouse_regService {
	@Autowired
	private Warehouse_regRepository warehouse_regRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private ObjectMapper om;

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(Warehouse_regServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) throws Exception {
		logger.info("Inside save method of Warehouse_regServiceImpl");
		Warehouse_reg warehouse_reg = om.readValue(data, Warehouse_reg.class);
		List<String> fileUploadList = new ArrayList<String>();
		if (!Objects.isNull(warehouse_reg.getIntId()) && warehouse_reg.getIntId() > 0) {
			Warehouse_reg getEntity = warehouse_regRepository.findByIntIdAndBitDeletedFlag(warehouse_reg.getIntId(),
					false);
			getEntity.setTxtapplicantname(warehouse_reg.getTxtapplicantname());
			getEntity.setTxtaddress(warehouse_reg.getTxtaddress());
			Warehouse_reg updateData = warehouse_regRepository.save(getEntity);
			parentId = updateData.getIntId();
			json.put("status", 202);
		} else {
			Warehouse_reg saveData = warehouse_regRepository.save(warehouse_reg);
			parentId = saveData.getIntId();
			json.put("status", 200);
		}
		json.put("id", parentId);
		return json;
	}

	@Override
	public JSONObject getById(Integer id) throws Exception {
		logger.info("Inside getById method of Warehouse_regServiceImpl");
		Warehouse_reg entity = warehouse_regRepository.findByIntIdAndBitDeletedFlag(id, false);

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) throws Exception {
		logger.info("Inside getAll method of Warehouse_regServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = warehouse_regRepository.countByBitDeletedFlag(false);
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent,
				Sort.by(Sort.Direction.DESC, "intId"));
		List<Warehouse_reg> warehouse_regResp = warehouse_regRepository.findAllByBitDeletedFlagAndIntInsertStatus(false,
				pageRequest);
		warehouse_regResp.forEach(entity -> {

		});
		json.put("status", 200);
		json.put("result", new JSONArray(warehouse_regResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(Integer id) throws Exception {
		logger.info("Inside deleteById method of Warehouse_regServiceImpl");
		Warehouse_reg entity = warehouse_regRepository.findByIntIdAndBitDeletedFlag(id, false);

		entity.setBitDeletedFlag(true);
		warehouse_regRepository.save(entity);
		json.put("status", 200);
		return json;
	}

}