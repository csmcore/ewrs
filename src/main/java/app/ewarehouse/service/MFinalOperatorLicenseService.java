package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.dto.WarehouseParticularsResponse;
import app.ewarehouse.entity.MFinalOperatorLicense;

public interface MFinalOperatorLicenseService {
    WarehouseParticularsResponse findByConformityIdAndBitDeleteFlag(String id);

    List<MFinalOperatorLicense> findAllByBitDeleteFlag();

    MFinalOperatorLicense getDetailsByLicenceId(String id);
}
