package app.ewarehouse.service;

import app.ewarehouse.entity.TwarehouseCharges;

public interface TwarehouseChargesService {

    TwarehouseCharges findByTxtConformityId(String txtConformityId);
}
