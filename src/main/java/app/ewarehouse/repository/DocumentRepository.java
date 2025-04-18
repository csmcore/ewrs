package app.ewarehouse.repository;

import app.ewarehouse.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Integer> {

}
