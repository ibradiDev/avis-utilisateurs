package ibradi.dev.avis.service;

import ibradi.dev.avis.entity.Jwt;
import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
	public static final String BEARER = "bearer";
	final String ENCRYPTION_KEY = "518477b84a55a00bf38eaadb8f9872f5883f10202cc3b60cd36c641432d6f872049c2da909ee9a967f99cd9744d702d5a05d5e585f5a6ebac0bb84a013dbf03c";
	private UtilisateurService utilisateurService;
	private JwtRepository jwtRepository;


	public Jwt tokenByValue(String token) {
		return jwtRepository
				.findByValue(token)
				.orElseThrow(() -> new RuntimeException("Token invalide ou inconnu"));
	}

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
		Utilisateur utilisateur =
				(Utilisateur) utilisateurService.loadUserByUsername(username);
		desableTokens(utilisateur);
		final Map<String, String> jwtMap = generateJwt(utilisateur);

		final Jwt jwt = Jwt.builder()
		                   .value(jwtMap.get(BEARER))
		                   .deactivated(false)
		                   .expired(false)
		                   .utilisateur(utilisateur)
		                   .build();
		jwtRepository.save(jwt);
		return jwtMap;
	}

	private void desableTokens(Utilisateur utilisateur) {
		final List<Jwt> jwtList = jwtRepository
				.findAllByUtilisateur(utilisateur.getEmail())
				.peek(jwt -> {
					jwt.setDeactivated(true);
					jwt.setExpired(true);
				}).toList();
		jwtRepository.saveAll(jwtList);
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
		return Map.of(BEARER, bearerToken);
	}

	private SecretKey getKey() {
		final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
		return Keys.hmacShaKeyFor(decoder);
	}

	public void deconnexion() {
		Utilisateur utilisateur =
				(Utilisateur) SecurityContextHolder
						.getContext().getAuthentication()
						.getPrincipal();

		Jwt jwt = jwtRepository
				.findUtilisateurValidToken(
						utilisateur.getEmail(), false, false
				).orElseThrow(() -> new RuntimeException("Token invalide"));

		jwt.setDeactivated(true);
		jwt.setExpired(true);
		jwtRepository.save(jwt);
	}

		@Scheduled(cron = "@daily")
//	@Scheduled(cron = "0 */1 * * * *")
	public void removeUseLessJwt() {
		log.info("Suppression des token à {}", Instant.now());
		jwtRepository.deleteAllByExpiredAndDeactivated(true, true);
	}
}