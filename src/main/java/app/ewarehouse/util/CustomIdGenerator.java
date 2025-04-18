package app.ewarehouse.util;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

public class CustomIdGenerator implements IdentifierGenerator, Configurable {
    private String tableName;
    private String idName;

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
        tableName = parameters.getProperty("tableName");
        idName = parameters.getProperty("idName");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection();
             CallableStatement callableStatement = connection.prepareCall("{CALL GenerateCustomID(?, ?, ?)}")) {
            callableStatement.setString(1, tableName);
            callableStatement.setString(2, idName);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            return callableStatement.getString(3);
        } catch (Exception e) {
            throw new RuntimeException("Error generating custom ID", e);
        }
    }
}

/*
  CREATE DEFINER=`ewrsdevu1`@`%` PROCEDURE `GenerateCustomID`(IN tableName VARCHAR(255), IN idName VARCHAR(255), OUT custom_id VARCHAR(20))
  BEGIN
      DECLARE prefix CHAR(1);
      DECLARE suffix CHAR(3);
      DECLARE digits1 CHAR(1);
      DECLARE digits2 CHAR(1);
      DECLARE digits3 CHAR(1);
      DECLARE mid_letter CHAR(1);
      DECLARE digit_mid CHAR(1);
      DECLARE letter_end CHAR(1);
      DECLARE digit_end1 CHAR(1);
      DECLARE digit_end2 CHAR(1);
      DECLARE new_id VARCHAR(20);

      DECLARE is_unique BOOLEAN DEFAULT FALSE;

      WHILE NOT is_unique DO
          SET prefix = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET suffix = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET digits1 = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET digits2 = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET digits3 = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET mid_letter = CHAR(FLOOR(0 + RAND() * 25) + 65); -- Random letter
          SET digit_mid = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET letter_end = CHAR(FLOOR(0 + RAND() * 25) + 65); -- Random letter
          SET digit_end1 = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit
          SET digit_end2 = CHAR(FLOOR(1 + RAND() * 9) + 48); -- Random digit

          SET new_id = CONCAT(prefix, mid_letter, digits1, digits2, digits3, mid_letter, digit_mid, letter_end, digit_end1, digit_end2);

          -- Check for uniqueness
          SET @sql = CONCAT('SELECT 1 FROM ', tableName, ' WHERE ', idName, ' = ?');
          PREPARE stmt FROM @sql;
          SET @param1 = new_id;
          EXECUTE stmt USING @param1;
          DEALLOCATE PREPARE stmt;

          IF NOT FOUND_ROWS() THEN
              SET is_unique = TRUE;
          END IF;
      END WHILE;

      SET custom_id = new_id;
  END
 */