package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.FocusAreaItem;

@Repository
public interface FocusAreaItemRepository extends JpaRepository<FocusAreaItem, Integer> {
}
