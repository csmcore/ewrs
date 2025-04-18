package app.ewarehouse.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.WarehouseParticularsResponse;
import app.ewarehouse.entity.CreatedStatus;
import app.ewarehouse.entity.MFinalOperatorLicense;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.MFinalOperatorlicenseRepository;
import app.ewarehouse.service.MFinalOperatorLicenseService;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.RoutineComplianceMapper;

@Service
public class MFinalOperatorLicenseServiceImpl implements MFinalOperatorLicenseService {
    @Autowired
    MFinalOperatorlicenseRepository repo;
    @Autowired
    ErrorMessages errorMessages;

    private static final Logger logger = LoggerFactory.getLogger( MFinalOperatorLicenseServiceImpl.class);

    @Override
    public WarehouseParticularsResponse findByConformityIdAndBitDeleteFlag(String id) {
        logger.info("Inside getOperator License");
        try{
            List<MFinalOperatorLicense> operatorLicense = repo.findByApplicationOfConformity_ApplicationIdAndBitDeletedFlagAndEnmStatus(id,false, CreatedStatus.Created);
            return  RoutineComplianceMapper.mapToWarehouseParticularsResponse((operatorLicense).get(0));
        }catch (Exception e){
            logger.info(e.getMessage());
            throw new CustomGeneralException(errorMessages.getParticularsNotFound());
        }
    }

    @Override
    public List<MFinalOperatorLicense> findAllByBitDeleteFlag() {
        return repo.findAllByBitDeletedFlag(false);
    }

	@Override
	public MFinalOperatorLicense getDetailsByLicenceId(String id) {
		return repo.findByVchLicenseNoAndBitDeletedFlag(id, false);
	}
}
