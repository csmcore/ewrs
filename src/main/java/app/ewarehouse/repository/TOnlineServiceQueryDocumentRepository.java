package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TOnlineServiceQueryDocument;
@Repository
public interface TOnlineServiceQueryDocumentRepository extends JpaRepository<TOnlineServiceQueryDocument, Integer> {

}
