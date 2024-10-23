package ibradi.dev.avis.au;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AvisService {

	private final AvisRepository avisRepository;
	private final UtilisateurRepository utilisateurRepository;

	public List<AvisDTO> search() {
		return avisRepository.findAll().stream()
		                     .map(avis ->
				                     new AvisDTO(avis.getMessage(), avis.getUtilisateur().getNom()))
		                     .collect(Collectors.toList());
	}

	public void create(AvisDTO avisDTO) {
		Utilisateur utilisateur = Utilisateur.builder().nom(avisDTO.nom()).build();
		utilisateur = utilisateurRepository.save(utilisateur);

		Avis avis = Avis.builder().message(avisDTO.message()).creation(LocalDateTime.now()).utilisateur(utilisateur).build();
		avisRepository.save(avis);
	}

}