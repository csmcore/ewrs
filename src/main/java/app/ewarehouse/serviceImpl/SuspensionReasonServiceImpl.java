package app.ewarehouse.serviceImpl;

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

import app.ewarehouse.entity.SuspensionReason;
import app.ewarehouse.repository.SuspensionReasonRepository;
import app.ewarehouse.service.SuspensionReasonService;
import jakarta.persistence.EntityManager;

@Service
public class SuspensionReasonServiceImpl implements SuspensionReasonService {
	@Autowired
	private SuspensionReasonRepository m_suspension_reasonRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
    private ObjectMapper om;

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(SuspensionReasonServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) throws Exception {
		logger.info("Inside save method of M_suspension_reasonServiceImpl");
		SuspensionReason m_suspension_reason = om.readValue(data, SuspensionReason.class);
		if (!Objects.isNull(m_suspension_reason.getIntId()) && m_suspension_reason.getIntId() > 0) {
			SuspensionReason getEntity = m_suspension_reasonRepository
					.findByIntIdAndBitDeletedFlag(m_suspension_reason.getIntId(), false);
			getEntity.setTxtReason(m_suspension_reason.getTxtReason());
			SuspensionReason updateData = m_suspension_reasonRepository.save(getEntity);
			parentId = updateData.getIntId();
			json.put("status", 202);
		} else {
			SuspensionReason saveData = m_suspension_reasonRepository.save(m_suspension_reason);
			parentId = saveData.getIntId();
			json.put("status", 200);
		}
		json.put("id", parentId);
		return json;
	}

	@Override
	public JSONObject getById(Integer id) throws Exception {
		logger.info("Inside getById method of M_suspension_reasonServiceImpl");
		SuspensionReason entity = m_suspension_reasonRepository.findByIntIdAndBitDeletedFlag(id, false);

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) throws Exception {
		logger.info("Inside getAll method of M_suspension_reasonServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = m_suspension_reasonRepository.countByBitDeletedFlag(false);
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent,
				Sort.by(Sort.Direction.DESC, "intId"));
		List<SuspensionReason> m_suspension_reasonResp = m_suspension_reasonRepository
				.findAllByBitDeletedFlagAndIntInsertStatus(false, pageRequest);
		m_suspension_reasonResp.forEach(entity -> {

		});
		json.put("status", 200);
		json.put("result", new JSONArray(m_suspension_reasonResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(Integer id) throws Exception {
		logger.info("Inside deleteById method of M_suspension_reasonServiceImpl");
		SuspensionReason entity = m_suspension_reasonRepository.findByIntIdAndBitDeletedFlag(id, false);

		entity.setBitDeletedFlag(true);
		m_suspension_reasonRepository.save(entity);
		json.put("status", 200);
		return json;
	}

	/**
	 * @return true - if data already exists, false - otherwise
	 */
	@Override
	public boolean checkDuplicateData(String data) {
		Integer count = m_suspension_reasonRepository.countDuplicateData(data);
		if(count!=null && count>0)
			return true;
		return false;
	}

}
