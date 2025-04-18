package app.ewarehouse.service;

import java.util.List;

import org.springframework.data.domain.Page;

import app.ewarehouse.dto.CountryResponse;
import app.ewarehouse.entity.Country;


public interface CountryService {
    void saveOrUpdate(String data);
    Page<CountryResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);
    List<CountryResponse> getAll();
    CountryResponse getById(Integer countryId);
    void toggleActivationStatus(String data);
    List<CountryResponse> getAllCountry();
	Page<CountryResponse> getCountryDetails(int page, int size, String search);
}
