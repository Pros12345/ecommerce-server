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

import jakarta.servlet.http.HttpServletResponse;

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

		logger.info("SecurityConfig : securityFilterChain :: Started");

		http

				/*
				 * Disable CSRF because this is a REST API using JWT authentication.
				 */
				.csrf(csrf -> csrf.disable())

				/*
				 * Enable CORS
				 */
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				/*
				 * Authentication / Authorization
				 */
				.authorizeHttpRequests(auth -> auth

						/*
						 * CORS preflight
						 */
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						/*
						 * Public APIs
						 */
						.requestMatchers("/api/auth/**", "/api/users/**", "/api/products/**", "/api/productsDisplay/**",
								"/api/images/**", "/api/permanent/**")
						.permitAll()

						/*
						 * Authenticated user APIs
						 */
						.requestMatchers("/api/user/**").authenticated()

						/*
						 * Everything else requires authentication
						 */
						.anyRequest().authenticated())

				/*
				 * Explicit authentication / authorization error handling.
				 */
				.exceptionHandling(exception -> exception

						/*
						 * 401 - User is not authenticated
						 */
						.authenticationEntryPoint((request, response, authException) -> {

							logger.error("401 Authentication failed: {} {} - {}", request.getMethod(),
									request.getRequestURI(), authException.getMessage());

							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						})

						/*
						 * 403 - User is authenticated but access is denied.
						 */
						.accessDeniedHandler((request, response, accessDeniedException) -> {

							logger.error("403 Access denied: {} {} - {}", request.getMethod(), request.getRequestURI(),
									accessDeniedException.getMessage());

							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						}))

				/*
				 * JWT filter must execute before UsernamePasswordAuthenticationFilter.
				 */
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		logger.info("SecurityConfig : securityFilterChain :: Ended");

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		logger.info("SecurityConfig : corsConfigurationSource :: Started");

		CorsConfiguration configuration = new CorsConfiguration();

		/*
		 * IMPORTANT:
		 *
		 * Use the exact production Angular/Vercel URL.
		 */
		configuration.setAllowedOrigins(
				List.of("http://localhost:4200", "https://ecommerce-client-pros12345s-projects.vercel.app"));

		/*
		 * Allowed HTTP methods
		 */
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		/*
		 * Authorization is included here because Angular sends:
		 *
		 * Authorization: Bearer <JWT>
		 */
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

		/*
		 * Required when frontend sends credentials.
		 */
		configuration.setAllowCredentials(true);

		/*
		 * Register CORS configuration for every endpoint.
		 */
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		logger.info("Allowed CORS origins: {}", configuration.getAllowedOrigins());

		logger.info("Allowed CORS methods: {}", configuration.getAllowedMethods());

		logger.info("SecurityConfig : corsConfigurationSource :: Ended");

		return source;
	}
}