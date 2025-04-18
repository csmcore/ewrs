package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ManageLanguage;

@Repository
public interface ManageLanguageRepository extends JpaRepository<ManageLanguage, Integer> {

	@Query("From ManageLanguage where bitDeletedFlag=:bitDeletedFlag and tinStatus=1 order by intId")
	List<ManageLanguage> getActiveLanguageList(Boolean bitDeletedFlag);

	
}
