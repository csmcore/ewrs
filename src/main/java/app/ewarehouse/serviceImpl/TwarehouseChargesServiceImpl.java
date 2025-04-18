package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.TwarehouseCharges;
import app.ewarehouse.repository.TwarehouseChargesRepository;
import app.ewarehouse.service.TwarehouseChargesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwarehouseChargesServiceImpl implements TwarehouseChargesService {

    @Autowired
    TwarehouseChargesRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(TwarehouseChargesServiceImpl.class);

    @Override
    public TwarehouseCharges findByTxtConformityId(String txtConformityId) {
        logger.info("txt conformity id"+txtConformityId);
        return repo.findByTxtConformityIdAndBitDeleteFlag(txtConformityId,false);
    }
}
