package app.ewarehouse.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaTest {

	public static void main(String[] args) {
		
		AtomicInteger seq = new AtomicInteger(10000);
		int nextVal = seq.incrementAndGet();
		
		String str= "[{\"id\":84,\"name\":\"Sorghum Five\"},{\"id\":101,\"name\":\"test123445\"}]";
	
		
		
		
		try {
			JSONArray array = new JSONArray(str);  
			
			System.out.println(array);
			
			for(int i=0; i < array.length(); i++)   
			{  
			JSONObject object = array.getJSONObject(i);  
			System.out.println(object.getInt("id"));  
			System.out.println(object.getString("name"));  
			}  
			  
			
		} catch (Exception e) {
			log.error("JavaTest:main()",e);
		}
		
	}

}
