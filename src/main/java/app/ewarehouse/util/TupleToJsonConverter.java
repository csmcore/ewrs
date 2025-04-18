package app.ewarehouse.util;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TupleToJsonConverter {

	 
	 public  String convertListTupleToJsonArray(List<Tuple> tupleList) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            ArrayNode jsonArray = objectMapper.createArrayNode();

	            for (Tuple tuple : tupleList) {
	                ObjectNode jsonObject = objectMapper.createObjectNode();
	                
	                // Loop through tuple elements
	                for (String alias : tuple.getElements().stream().map(e -> e.getAlias()).toList()) {
	                    jsonObject.put(alias, tuple.get(alias).toString());  // Convert each tuple value to JSON
	                }

	                jsonArray.add(jsonObject);
	            }

	            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonArray);
	        } catch (Exception e) {
	        	log.info("Inside create method of TupleToJsonConverterl"+e.getMessage());
	            return null; // Return empty JSON array on error
	        }
	    }
}
