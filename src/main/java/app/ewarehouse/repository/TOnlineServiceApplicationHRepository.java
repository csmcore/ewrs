package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TOnlineServiceApplicationH;
@Repository
public interface TOnlineServiceApplicationHRepository extends JpaRepository<TOnlineServiceApplicationH, Integer> {

}
