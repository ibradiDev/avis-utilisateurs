package ibradi.dev.avis.controller;

import ibradi.dev.avis.entity.Avis;
import ibradi.dev.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("avis")
public class AvisController {
	private final AvisService avisService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void creer(@RequestBody Avis avis) {
		avisService.creer(avis);
	}

}