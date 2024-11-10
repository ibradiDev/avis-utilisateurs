package ibradi.dev.avis.security;

import ibradi.dev.avis.entity.Jwt;
import ibradi.dev.avis.service.JwtService;
import ibradi.dev.avis.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UtilisateurService utilisateurService;

	public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
		this.utilisateurService = utilisateurService;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String token = null;
		Jwt tokenFromDB = null;
		String username = null;
		boolean isTokenExpired = true;

		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")) {
			token = request.getHeader("Authorization").substring(7);
			tokenFromDB = jwtService.tokenByValue(token);
			isTokenExpired = jwtService.isTokenExpired(token);
			username = jwtService.extractUsername(token);
		}

		if (!isTokenExpired
				&& tokenFromDB.getUtilisateur().getEmail().equals(username)
				&& SecurityContextHolder.getContext().getAuthentication() == null
		) {
			UserDetails userDetails = utilisateurService.loadUserByUsername(username);
//			Créer mes données d'authentification
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities()
					);
//			Authentifier l'utilisateur
			SecurityContextHolder.getContext()
			                     .setAuthentication(usernamePasswordAuthenticationToken);
		}
//		Continuer le traitement (filtrage avec Spring security)
		filterChain.doFilter(request, response);
	}
}