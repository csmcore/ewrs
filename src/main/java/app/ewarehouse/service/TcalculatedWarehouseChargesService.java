package app.ewarehouse.service;

import app.ewarehouse.entity.TcalculatedWarehouseCharges;

public interface TcalculatedWarehouseChargesService {
    TcalculatedWarehouseCharges save(String data);

    String getChargesForWarehouseReceipt(String id);
}
