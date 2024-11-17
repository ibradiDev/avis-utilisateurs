package ibradi.dev.avis.service;

import ibradi.dev.avis.entity.Avis;
import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.repository.AvisRepository;
import ibradi.dev.avis.repository.JwtRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AvisService {
	private final AvisRepository avisRepository;
	JwtRepository jwtRepository;

	public void creer(Avis avis) {
//		Récupération de l'utilisateur connecté
		Utilisateur utilisateur = (Utilisateur) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		avis.setUtilisateur(utilisateur);
		jwtRepository
				.findUtilisateurValidToken(
						utilisateur.getEmail(), false, false
				).orElseThrow(() -> new RuntimeException("Token invalide"));

		avisRepository.save(avis);
	}
}