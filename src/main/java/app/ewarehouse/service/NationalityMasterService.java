package app.ewarehouse.service;

import java.util.List;
import java.util.Optional;

import app.ewarehouse.dto.NationalityMasterDto;
import app.ewarehouse.entity.Nationality;

public interface NationalityMasterService {

	List<Nationality> getAllNationalities();

    Optional<Nationality> getNationalityById(Long id);

    Nationality createNationality(NationalityMasterDto nationality);

    Nationality updateNationality(Long id, NationalityMasterDto nationalityDto);

    void changeNationalityStatus(Long id);

	List<Nationality> getNationalities();
	
}
