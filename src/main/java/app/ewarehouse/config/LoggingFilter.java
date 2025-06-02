package app.ewarehouse.config;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class LoggingFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        long startTime = System.currentTimeMillis();

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        StringBuilder parameterString = new StringBuilder();
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<String> keySet = parameterMap.keySet();
        for(String key : keySet) {
        	parameterString.append(key);
        	parameterString.append("=");
        	parameterString.append(parameterMap.get(key)[0]);
        	parameterString.append(", ");
        }
        logger.info("Request: {} {} :: Parameters: {}", method, requestURI, parameterString);

        try {
        	chain.doFilter(request, response);
        }
        catch (Exception e) {
			logger.error("Exception caught in filter: ", e);
		}

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        logger.info("Finished processing {} request for {} in {} ms with response status {}",
        		method, requestURI, duration, httpResponse.getStatus());
	}
	
	@Override
    public void destroy() {}

}
