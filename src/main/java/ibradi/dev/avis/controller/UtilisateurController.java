package ibradi.dev.avis.controller;

import ibradi.dev.avis.AuthentificationDTO;
import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.service.JwtService;
import ibradi.dev.avis.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {

	private AuthenticationManager authenticationManager;
	private UtilisateurService utilisateurService;
	private JwtService jwtService;

	@PostMapping(path = "inscription")
	public void inscription(@RequestBody Utilisateur utilisateur) {
		log.info("Inscription");
		utilisateurService.inscription(utilisateur);
	}

	@PostMapping(path = "activation")
	public void activation(@RequestBody Map<String, String> activation) {
		log.info("Activation");
		utilisateurService.activation(activation);
	}

	@PostMapping(path = "connexion")
	public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authentificationDTO.username(), authentificationDTO.password())
		);
		if (authentication.isAuthenticated()) {
			log.info("Connexion");
			return jwtService.generate(authentificationDTO.username());
		}
		return null;
	}
}