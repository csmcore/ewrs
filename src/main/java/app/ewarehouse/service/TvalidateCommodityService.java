package app.ewarehouse.service;

import app.ewarehouse.entity.TvalidateCommodity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TvalidateCommodityService {
    List<TvalidateCommodity> findAll();

    String save(String data);

    Page<TvalidateCommodity> getAllCommodities(int page, int size);

    TvalidateCommodity getCommodityByValidateId(String id);
}
