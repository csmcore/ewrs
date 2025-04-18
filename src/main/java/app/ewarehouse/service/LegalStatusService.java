package app.ewarehouse.service;

import app.ewarehouse.entity.LegalStatus;

import java.util.List;

public interface LegalStatusService {
    Integer save(String legalStatus);
    LegalStatus getById(Integer Id);
    List<LegalStatus> getAll();
}
