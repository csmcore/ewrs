package app.ewarehouse.repository;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Repository
public class ProcedureCall {

	@PersistenceContext
    private EntityManager entityManager;
	
	  public List<String>  getValidReceiptDepositorById(String depositor_id) {
	        StoredProcedureQuery storedProcedure = entityManager.createNamedStoredProcedureQuery("DepositorValidReceiptNoGet");
	        storedProcedure.setParameter("depositor_id", depositor_id);
	        // Execute the stored procedure
	        storedProcedure.execute();
	        // Retrieve the OUT parameter result
	          List<String> rs=   storedProcedure.getResultList();
	        System.out.println(rs);
	        return rs;
	    }
}
