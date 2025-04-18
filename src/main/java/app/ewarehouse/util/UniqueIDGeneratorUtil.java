package app.ewarehouse.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;

import jakarta.persistence.Query;

public class UniqueIDGeneratorUtil implements IdentifierGenerator, Configurable {
	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyyMMdd"; // Format: YYYYMMDD
    private String prefix;
    private String tableName;
    private String idColumnName;

    
    public void configure(Map<String, Object> params) {
        this.prefix = (String) params.get("prefix");
        this.tableName = (String) params.get("tableName");
        this.idColumnName = (String) params.get("idName");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        // Get current date in YYYYMMDD format
        String currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        // Construct SQL query pattern (e.g., "WAR20250212%")
        String pattern = prefix + currentDate + "%";

        // Construct SQL Query
        String sql = "SELECT MAX(" + idColumnName + ") FROM " + tableName + " WHERE " + idColumnName + " LIKE :pattern";

        Query query = session.createNativeQuery(sql);
        query.setParameter("pattern", pattern);

        List<?> result = query.getResultList();
        String lastId = (result.isEmpty() || result.get(0) == null) ? null : result.get(0).toString();

        int newIndex = 1; // Default starting index

        if (lastId != null && lastId.startsWith(prefix + currentDate)) {
            // Extract last 4-digit sequence and increment
            String lastIndexStr = lastId.substring(lastId.length() - 4);
            newIndex = Integer.parseInt(lastIndexStr) + 1;
        }

        // Format new index as a 4-digit number (e.g., 0001, 0002)
        String newIndexStr = String.format("%04d", newIndex);

        // Construct and return the new ID
        return prefix + currentDate + newIndexStr;
    }

    public boolean supportsEventType(EventType eventType) {
        return eventType == EventType.INSERT;
    }
}
