package eCommerse.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	PasswordEncoder passwordEncoder() {

		logger.info("SecurityConfig : passwordEncoder :: Started");
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {

		logger.info("SecurityConfig : authManager :: Started");
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		logger.info("SecurityConfig : securityFilterChain :: Started");
		http.cors(cors -> {
		}).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth
								.requestMatchers("/api/auth/**", "/api/users/**", "/api/products/**",
										"/api/productsDisplay/**", "/api/images/**")
								.permitAll().anyRequest().authenticated());

		logger.info("SecurityConfig : securityFilterChain :: Ended");

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		logger.info("SecurityConfig : corsConfigurationSource :: Started");

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200",
				"https://ecommerce-client-git-firstproject-pros12345s-projects.vercel.app",
				"https://ecommerce-client-mu-ten.vercel.app",
				"https://ecommerce-client-7hsj2lhwp-pros12345s-projects.vercel.app"));

		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		logger.info("SecurityConfig : corsConfigurationSource :: Ended");

		return source;
	}
}