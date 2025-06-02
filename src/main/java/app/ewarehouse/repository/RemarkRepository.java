package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.entity.Remark;

public interface RemarkRepository extends JpaRepository<Remark, Integer> {

}
