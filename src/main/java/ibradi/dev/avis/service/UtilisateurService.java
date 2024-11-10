package ibradi.dev.avis.service;

import ibradi.dev.avis.RoleType;
import ibradi.dev.avis.entity.Role;
import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.entity.Validation;
import ibradi.dev.avis.repository.UtilisateurRepository;
import ibradi.dev.avis.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
	private ValidationRepository validationRepository;
	private UtilisateurRepository utilisateurRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private ValidationService validationService;

	public void inscription(Utilisateur utilisateur) {

		if (!utilisateur.getEmail().contains("@"))
			throw new RuntimeException("Email invalide");
		if (!utilisateur.getEmail().contains("."))
			throw new RuntimeException("Email invalide");

		Optional<Utilisateur> utilisateurOptionel = utilisateurRepository
				.findByEmail(utilisateur.getEmail());
		if (utilisateurOptionel.isPresent())
			throw new RuntimeException("Cet email existe déjà");

		String mdpCrypte = passwordEncoder.encode(utilisateur.getMdp());
		utilisateur.setMdp(mdpCrypte);

		Role roleUtilisateur = new Role();
		roleUtilisateur.setLabel(RoleType.UTILISATEUR);
		utilisateur.setRole(roleUtilisateur);

		utilisateur = utilisateurRepository.save(utilisateur);
		validationService.enregister(utilisateur);
	}

	public void activation(Map<String, String> activation) {
		String codeValidation = activation.get("code");
		Validation validation = validationService.lireEnFonctionDuCode(codeValidation);
		if (Instant.now().isAfter(validation.getExpiration()))
			throw new RuntimeException("Votre code de validation a expiré");

		Utilisateur utilisateurActive = utilisateurRepository
				.findById(validation.getUtilisateur().getId())
				.orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));

		validation.setActivation(Instant.now());
		utilisateurActive.setActif(true);
		validationRepository.save(validation);
		utilisateurRepository.save(utilisateurActive);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return utilisateurRepository
				.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Cet utilisateur n'existe pas"));
	}


}