package app.ewarehouse.service;

import app.ewarehouse.entity.Bank;
import app.ewarehouse.entity.BuyerDepositorType;

import java.util.List;

public interface BuyerDepositorTypeService {
    Integer save(String buyerDepositorType);
    BuyerDepositorType getById(Integer Id);
    List<BuyerDepositorType> getAll();
}
