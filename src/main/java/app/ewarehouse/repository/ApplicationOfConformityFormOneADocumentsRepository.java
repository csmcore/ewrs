package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformityFormOneADocuments;

@Repository
public interface ApplicationOfConformityFormOneADocumentsRepository extends JpaRepository<ApplicationOfConformityFormOneADocuments, Long>{

}
