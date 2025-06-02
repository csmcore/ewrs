package app.ewarehouse.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import app.ewarehouse.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	@Autowired
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private final UserService userService;
	private static final String[] WHITELIST_URLS = { "/logindetails", "/generateCaptcha", "/getotp", "/checkotp",
			"/createTempUser", "/web/**", "/getVersion", "/conformity/paymentReturn",
			"/complaint-management/complaint-management/download/**", "/conformity/download/**",
			"/admin/operator-licences/oneCDoc/**", "/admin/operator-licences/inspReport/download/**", "/api/trade/**" };

	// private static final String[] WHITELIST_URLS = { "/**" };
	/*
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
	 * throws Exception { http.csrf(AbstractHttpConfigurer::disable)
	 * .authorizeHttpRequests(request ->
	 * request.requestMatchers(WHITELIST_URLS).permitAll()
	 * .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name()).
	 * requestMatchers("/user/**")
	 * .hasAnyAuthority(Role.USER.name()).anyRequest().authenticated())
	 * .sessionManagement(manager ->
	 * manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	 * .authenticationProvider(authenticationProvider())
	 * .addFilterBefore(jwtAuthenticationFilter,
	 * UsernamePasswordAuthenticationFilter.class); return http.build(); }
	 */

	/*
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
	 * throws Exception {
	 * http.csrf().disable().authorizeHttpRequests().requestMatchers(WHITELIST_URLS)
	 * .permitAll().anyRequest() .authenticated().and()
	 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
	 * and() .authenticationProvider(authenticationProvider())
	 * .addFilterBefore(jwtAuthenticationFilter,
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * return http.build(); }
	 */

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable).headers().cacheControl().disable() // Disable default
																							// Cache-Control header
				.xssProtection().disable() // Disable XSS Protection header
				.addHeaderWriter((request, response) -> {
					response.addHeader("X-Custom-Header", "CustomValue");
				}).and()
				.authorizeHttpRequests(
						request -> request.requestMatchers(WHITELIST_URLS).permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		// configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT"));

		configuration.setAllowedHeaders(List.of("Referrer-Policy", "strict-origin-when-cross-origin",
				"Access-Control-Allow-Headers", "Authorization", "Content-Type", "X-Auth-Token", "Origin",
				"X-Requested-With", "Accept", "Audit-Trail", "x-user-type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
