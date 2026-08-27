package eCommerse.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {

		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

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

		http.csrf(csrf -> csrf.disable())

				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						.requestMatchers("/api/auth/**", "/api/users/**", "/api/products/**", "/api/productsDisplay/**",
								"/api/images/**", "/api/permanent/**")
						.permitAll()

						.requestMatchers("/api/user/**").authenticated()

						.anyRequest().authenticated())

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
				List.of("http://localhost:4200", "https://ecommerce-client-pros12345s-projects.vercel.app"));

		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}