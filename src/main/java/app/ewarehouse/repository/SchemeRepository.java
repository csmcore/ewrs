package app.ewarehouse.repository;

import app.ewarehouse.entity.MScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeRepository extends JpaRepository<MScheme, Integer> {
}
