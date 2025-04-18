package app.ewarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;


@SpringBootApplication
@EnableCaching
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan("app.ewarehouse")
public class EwarehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EwarehouseApplication.class, args);
	}
	/*
	 * Users adminAccount = userRepository.findByRole(Role.ADMIN); 
	 * if(adminAccount
	 * == null) { Users user = new Users(); user.setEmail("admin@gmail.com");
	 * user.setFirstname("admin"); user.setSecondname("admin");
	 * user.setRole(Role.ADMIN); user.setPassword(new
	 * BCryptPasswordEncoder().encode("admin")); userRepository.save(user); }
	 */
	/*
	 * @Bean public HttpMessageConverter<BufferedImage>
	 * createImageHttpMessageConverter() { return new
	 * BufferedImageHttpMessageConverter(); }
	 */

}
