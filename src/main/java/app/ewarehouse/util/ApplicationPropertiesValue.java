package app.ewarehouse.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationPropertiesValue {
	
	 @Value("${payment.secretkey}")
	    private String secretKey;

	    @Value("${payment.apiClientID}")
	    private String apiClientID;

	    @Value("${payment.serviceID}")
	    private String serviceID;

	    public String getSecretKey() {
	        return secretKey;
	    }

	    public String getApiClientID() {
	        return apiClientID;
	    }

	    public String getServiceID() {
	        return serviceID;
	    }

}
