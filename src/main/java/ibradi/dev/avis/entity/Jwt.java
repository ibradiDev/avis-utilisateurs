package ibradi.dev.avis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "jwt")
public class Jwt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String value;
	private boolean deactivated;
	private boolean expired;

	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
	@JoinColumn(name = "utilisateur_id")
	private Utilisateur utilisateur;
}