package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query("SELECT c FROM Country c WHERE c.countryId = :id")
    Optional<Country> findCountryById(@Param("id") Integer id);
    
    @Query("SELECT c FROM Country c ORDER BY c.countryId DESC")
    Page<Country> findAllCountries(Pageable pageable);

    @Query("SELECT c FROM Country c " +
            "WHERE (:search IS NULL OR " +
            "LOWER(c.countryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.countryCode) LIKE LOWER(CONCAT('%', :search, '%'))) order by c.countryId desc")
    Page<Country> findByFilters(@Param("search") String search, Pageable pageable);
    
    
    
      List<Country> findByIsActiveTrue();
    
    
}
