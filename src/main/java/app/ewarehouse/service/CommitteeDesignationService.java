package app.ewarehouse.service;

import app.ewarehouse.entity.CommitteeDesignation;
import app.ewarehouse.entity.LegalStatus;

import java.util.List;

public interface CommitteeDesignationService {
    Integer save(String committeeDesignation);
    CommitteeDesignation getById(Integer Id);
    List<CommitteeDesignation> getAll();
}
