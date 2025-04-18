package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Seasonality;

@Repository
public interface SeasonalityRepository extends JpaRepository<Seasonality, Integer>{
   
	@Query(value="select Id, name from t_seasonality;",nativeQuery=true)
	List<Object[]> getAllSeason();

}
