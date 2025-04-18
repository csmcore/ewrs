package app.ewarehouse.service;

import app.ewarehouse.dto.ApplicationCollateralDTO;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TapplicationOfCertificateOfCompliance;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TapplicationOfCertificateOfComplianceService {
   
    ApplicationCollateralDTO save(String certApplication) throws JsonProcessingException;

    List<TapplicationOfCertificateOfCompliance> findAll() ;

    ApplicationCollateralDTO getApplicationById(String userId);

    ApplicationCollateralDTO getApplication(Integer roleId, String status);

    Page<ApplicationCollateralDTO> findByFilters(Integer intCurrentRole, Integer userId, Status status, String search, String sortColumn, String sortDirection, Pageable pageable);

    String takeAction(String data);

    List<ApplicationCollateralDTO> getApplicationBySubCountyId(Integer subCountyId);

    List<SubCountyResponse> findAllsubCounty();
}
