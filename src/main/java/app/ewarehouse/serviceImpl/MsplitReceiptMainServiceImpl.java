package app.ewarehouse.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.MsplitReceiptResponse;
import app.ewarehouse.dto.TsplitReceiptResponse;
import app.ewarehouse.entity.MsplitReceiptMain;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsplitReceipt;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.enmReceiptStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.exception.ResourceNotFoundException;
import app.ewarehouse.repository.MsplitReceiptMainRepository;
import app.ewarehouse.repository.TsplitReceiptRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.MsplitReceiptMainService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.Mapper;

@Service
public class MsplitReceiptMainServiceImpl implements MsplitReceiptMainService {

	private static final Logger logger = LoggerFactory.getLogger(MsplitReceiptMainServiceImpl.class);

	@Autowired
	MsplitReceiptMainRepository repo;
	@Autowired
	ErrorMessages errorMessages;
	@Autowired
	TwarehouseReceiptRepository receiptRepository;
	@Autowired
	TsplitReceiptRepository tsplitReceiptRepository;

	@Autowired
	private ObjectMapper om;

	@Override
	public List<MsplitReceiptResponse> findAll() {
		List<MsplitReceiptMain> msplitReceiptMainList = repo.findAllByBitDeleteFlagAndStatus(false,
				enmReceiptStatus.Split);
		return msplitReceiptMainList.stream().map(Mapper::mapToMsplitReceiptResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String save(String data) {
		logger.info("inside m split service save");
		try {
			String decodedData = CommonUtil.inputStreamDecoder(data);
			MsplitReceiptMain splitReceipt = om.readValue(decodedData, MsplitReceiptMain.class);
			splitReceipt.setStatus(enmReceiptStatus.Split);
			splitReceipt.setOldWarehouseReceipt(JsonFileExtractorUtil.uploadFile(splitReceipt.getOldWarehouseReceipt(),
					FolderAndDirectoryConstant.SPLIT_RECEIPT_MAIN));
			String warehouseReceiptId = ((splitReceipt.getWarehouseReceipt().getTxtWarehouseReceiptId()));

			MsplitReceiptMain savedSplitReceipt = repo.save(splitReceipt);
			TwarehouseReceipt receipt = receiptRepository
					.findByTxtWarehouseReceiptIdAndStatusAndBitDeleteFlag(warehouseReceiptId, Status.Approved, false);

			if (receipt != null) {
				receipt.setReceiptStatus(enmReceiptStatus.Split);
				splitReceipt.setWarehouseReceipt(receipt);
				receiptRepository.save(receipt);
			} else {
				throw new CustomGeneralException(errorMessages.getReceiptNotFound());
			}

			for (TsplitReceipt child : splitReceipt.getSplits()) {
				child.setSplitReceipt(savedSplitReceipt);
				child.setDtmCreatedOn(new Date());
			}
			return savedSplitReceipt.getTxtSplitApplicationId();
		} catch (Exception e) {
			throw new CustomGeneralException(e + "error occured" + errorMessages.getUnknownError());
		}
	}

	@Override
	public TsplitReceiptResponse getReceiptBySplitReceiptId(String id) {
		TsplitReceipt receipt = tsplitReceiptRepository.getBySplitReceiptNoAndBitDeleteFlag(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(errorMessages.getSplitReceiptNotFound() + " " + id));
		return Mapper.mapToSplitReceiptResponse(receipt);
	}

	@Override
	public Page<TsplitReceiptResponse> getFilteredReceipts(String search, String sortColumn, String sortDirection,
			Pageable pageable) {
		Page<TsplitReceipt> splitReceiptsPage = tsplitReceiptRepository.findByFilters(search, pageable);
		List<TsplitReceiptResponse> response = splitReceiptsPage.getContent().stream()
				.map(Mapper::mapToSplitReceiptResponse).collect(Collectors.toList());

		return new PageImpl<>(response, pageable, splitReceiptsPage.getTotalElements());
	}

	@Override
	public boolean checkIfSplitReceiptNoExists(String id) {
		return tsplitReceiptRepository.getBySplitReceiptNoAndBitDeleteFlag(id, false).isPresent();
	}
}
