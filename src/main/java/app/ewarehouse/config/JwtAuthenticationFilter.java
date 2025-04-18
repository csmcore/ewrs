package app.ewarehouse.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.ewarehouse.entity.AuditTrail;
import app.ewarehouse.repository.AuditTrailRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.JwtService;
import app.ewarehouse.service.UserService;
import app.ewarehouse.util.CommonUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private final JwtService jwtService;

	@Autowired
	private final UserService userService;

	@Autowired
	private TuserRepository tuserRepo;
	@Autowired
	private MroleRepository mroleRepo;

	@Autowired
	private AuditTrailRepository auditTrailRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

//		System.out.println(request.toString());
//		System.out.println(response.toString());
//		printRequestDetails(request);
		
		try {
			final String authHeader = request.getHeader("Authorization");
			final String jwt;
			final String userEmail;
			int responseStatus = response.getStatus(); 
			String encodedData = request.getHeader("Audit-Trail");
			
			Runnable runnable = () -> {

				if (encodedData != null) {
					try {
						String modifiedString = encodedData.substring(10);
						byte[] decodedBytes = Base64.getDecoder().decode(modifiedString);
						String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
						JSONObject jsonObject = new JSONObject(decodedString);
						int userId = jsonObject.getInt("userId");
						int roleId = jsonObject.getInt("roleId");
						String ipAddress = jsonObject.getString("ipAddress");
						double latitude = jsonObject.getDouble("latitude");
						double longitude = jsonObject.getDouble("longitude");
						String browserDetails = jsonObject.getString("browserDetails");
						String osDetails = jsonObject.getString("osDetails");
						String action = jsonObject.getString("action");
						AuditTrail ad = new AuditTrail();
						if (userId != 0) {
							ad.setUser(tuserRepo.findByIntIdAndBitDeletedFlag(userId, false));
						} else {
							ad.setUser(null);
						}
						if (roleId != 0) {
							ad.setRole(mroleRepo.findByIntIdAndBitDeletedFlag(roleId, false));
						} else {
							ad.setRole(null);
						}
						ad.setIpAddress(ipAddress);
						ad.setLatitude(Double.toString(latitude));
						ad.setLongitude(Double.toString(longitude));
						ad.setDeviceName(browserDetails);
						ad.setDtmCreatedOn(LocalDateTime.now());
						ad.setOs(osDetails);
						ad.setAction(action);
						ad.setRemarks("This is backend URL and the response status is : " + responseStatus);
						auditTrailRepo.saveAndFlush(ad);

					} catch (IllegalArgumentException e) {
						System.err.println("Failed to decode Base64: " + e.getMessage());
					} catch (JSONException e) {
						System.err.println("Failed to parse JSON: " + e.getMessage());
					}
				}
			};

			new Thread(runnable).start();
			 

			// Continue with the filter chain
			// filterChain.doFilter(request, response); This line is responsible to send the
			// request response for further action

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);

			}

			else {

				jwt = authHeader.substring(7);

				String[] chunks = jwt.split("\\.");

				Base64.Decoder decoder = Base64.getUrlDecoder();
				String header = new String(decoder.decode(chunks[0]));
				String payload = new String(decoder.decode(chunks[1]));
				JSONObject jsonobj = new JSONObject(payload);
				String roleId = jsonobj.get("roleId").toString();
				String userid = jsonobj.get("userid").toString();
				String email = jsonobj.get("email").toString();
				String username = jsonobj.get("username").toString();

				request.setAttribute("roleId", roleId);
				request.setAttribute("email", email);
				request.setAttribute("userid", userid);
				request.setAttribute("username", username);


				userEmail = jwtService.extractUserName(jwt);
				if (StringUtils.isNotEmpty(userEmail)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
					if (jwtService.isTokenValid(jwt, userDetails)) {
						SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
								null, userDetails.getAuthorities());
						token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						securityContext.setAuthentication(token);
						SecurityContextHolder.setContext(securityContext);
					}
				}

				filterChain.doFilter(request, response);

			}
		} catch (Exception e) {
			JSONArray jsonarr = new JSONArray();
			
			  JSONObject output = new JSONObject(); 
			  output.put("msg", "Token_Time_Out");
			  output.put("status", 600); 
			  JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
			  jsonarr.put(login_jsonobj);
			// Set response content type
		      response.setContentType("application/json");
		      response.setCharacterEncoding("UTF-8");
			  response.setStatus(200);
			// Convert to JSON string
		       // ObjectMapper objectMapper = new ObjectMapper();
		        String jsonString;
		        
		        System.out.println("Token expired");
				try {
					//jsonString = objectMapper.writeValueAsString(jsonarr);
					 response.getWriter().write(login_jsonobj.toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getMessage();
				}
		        // Write JSON to response
		       
			  
			//throw new JwtTimeOutException(" jwt token expierd");
		}

	}
	
	
	public void printRequestDetails(HttpServletRequest request) throws IOException {
	    System.out.println("----- HTTP REQUEST -----");

	    System.out.println("Method: " + request.getMethod());
	    System.out.println("Request URI: " + request.getRequestURI());

	    System.out.println("Headers:");
	    Enumeration<String> headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String headerName = headerNames.nextElement();
	        System.out.println(headerName + ": " + request.getHeader(headerName));
	    }

	    System.out.println("Parameters:");
	    request.getParameterMap().forEach((key, value) -> 
	        System.out.println(key + ": " + Arrays.toString(value)));

	    // Read body (Note: only works once unless request is wrapped)
	    StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }
	    System.out.println("Body: " + sb.toString());
	}

}
