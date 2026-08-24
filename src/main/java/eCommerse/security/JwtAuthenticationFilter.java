package eCommerse.security;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("JWT FILTER STARTED");

		String requestUri = request.getRequestURI();

		logger.info("Request: {} {}", request.getMethod(), requestUri);

		String authHeader = request.getHeader("Authorization");

		logger.info("Authorization Header: {}", authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			logger.info("No Bearer token found");

			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		try {

			String email = jwtUtil.extractUsername(token);

			logger.info("Email extracted from JWT: {}", email);

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
						null, Collections.emptyList());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				logger.info("Authentication successfully set for: {}", email);
			}

		} catch (Exception e) {

			logger.error("JWT validation failed", e);

			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}