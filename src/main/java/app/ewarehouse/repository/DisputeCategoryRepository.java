package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.DisputeCategory;

@Repository
public interface DisputeCategoryRepository extends JpaRepository<DisputeCategory, Integer> {
    @Query("SELECT d FROM DisputeCategory d WHERE d.isDeleted = :booleanDeletedFlag")
    List<DisputeCategory> findAllDisputeCategories(@Param("booleanDeletedFlag") Boolean booleanDeletedFlag);

    @Query("SELECT d FROM DisputeCategory d WHERE d.disputeCategoryId = :id and d.isDeleted = :booleanDeletedFlag")
    Optional<DisputeCategory> findDisputeCategoryById(@Param("id") Integer id, @Param("booleanDeletedFlag") Boolean booleanDeletedFlag);

}
