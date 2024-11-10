package ibradi.dev.avis.security;

import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {
	final String ENCRYPTION_KEY = "518477b84a55a00bf38eaadb8f9872f5883f10202cc3b60cd36c641432d6f872049c2da909ee9a967f99cd9744d702d5a05d5e585f5a6ebac0bb84a013dbf03c";
	private UtilisateurService utilisateurService;

	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder()
		           .setSigningKey(getKey())
		           .build()
		           .parseClaimsJws(token)
		           .getBody();
	}

	private <T> T getClaim(String token, Function<Claims, T> function) {
		final Claims claims = getAllClaims(token);
		return function.apply(claims);
	}


	public Boolean isTokenExpired(String token) {
		Date expirationDate = getClaim(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}


	public String extractUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public Map<String, String> generate(String username) {
		Utilisateur utilisateur = (Utilisateur) utilisateurService.loadUserByUsername(username);

		return generateJwt(utilisateur);
	}

	private Map<String, String> generateJwt(Utilisateur utilisateur) {
		final long currentTime = System.currentTimeMillis();
		final long expirationTime = currentTime + (30 * 60 * 1000);

		Map<String, Object> claims = Map.of(
				"nom", utilisateur.getNom(),
				Claims.EXPIRATION, new Date(expirationTime),
				Claims.SUBJECT, utilisateur.getEmail()
		);

		final String bearerToken = Jwts.builder()
		                               .setIssuedAt(new Date(currentTime))
		                               .setExpiration(new Date(expirationTime))
		                               .setClaims(claims)
		                               .signWith(getKey(), SignatureAlgorithm.HS512)
		                               .compact();
		return Map.of("bearer", bearerToken);
	}

	private SecretKey getKey() {
		final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
		return Keys.hmacShaKeyFor(decoder);
	}


}