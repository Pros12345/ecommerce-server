package eCommerse.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "mySuperSecretKeyThatIsAtLeast32CharsLong!";

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 10;

	private Key getSigningKey() {

		logger.info("JwtUtil : getSigningKey :: Started");

		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String username) {

		logger.info("JwtUtil : generateToken :: Started");

		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + EXPIRATION_TIME);

		logger.info("JwtUtil : generateToken :: Ended");

		return Jwts.builder().setSubject(username).setIssuedAt(issuedAt).setExpiration(expiration)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {

		logger.info("JwtUtil : extractUsername :: Started");

		return extractAllClaims(token).getSubject();
	}

	public boolean validateToken(String token, String username) {

		logger.info("JwtUtil : validateToken :: Started");

		final String extractedUsername = extractUsername(token);

		return extractedUsername.equals(username) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {

		logger.info("JwtUtil : isTokenExpired :: Started");

		return extractAllClaims(token).getExpiration().before(new Date());
	}

	private Claims extractAllClaims(String token) {

		logger.info("JwtUtil : extractAllClaims :: Started");

		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}