package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DisputeSupportingDocumentType;

@Repository
public interface DisputeSupportingDocTypeRepository extends JpaRepository<DisputeSupportingDocumentType, Integer> {

    @Query("SELECT d FROM DisputeSupportingDocumentType d WHERE d.isDeleted = :booleanDeletedFlag")
    List<DisputeSupportingDocumentType> findAllDisputeSupportingDocumentType(@Param("booleanDeletedFlag") Boolean booleanDeletedFlag);
}
