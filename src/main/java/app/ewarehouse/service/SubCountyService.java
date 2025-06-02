package app.ewarehouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import app.ewarehouse.dto.ApprovedSubCountyDto;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.SubCounty;

public interface SubCountyService {
	List<SubCountyResponse> getAllSubCounties();
    Optional<SubCountyResponse> getSubCountyById(Integer id);
    SubCountyResponse createSubCounty(String subCounty);
    SubCountyResponse updateSubCounty(Integer id, String subCounty);
    void deleteSubCounty(Integer id);
    List<SubCountyResponse> getSubCountiesByCountyId(Integer countyId);

    List<SubCountyResponse> getAllSubCountiesActiveAndInactive();
	List<ApprovedSubCountyDto> getApprovedSubCounties(Integer countyId,Integer roleId);
	 Page<SubCountyResponse> getAllSubCountiesList(Integer pageNumber, Integer pageSize, String sortCol);
	void delete(String data);
}
