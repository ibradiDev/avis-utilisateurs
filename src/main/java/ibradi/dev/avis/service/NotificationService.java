package ibradi.dev.avis.service;

import ibradi.dev.avis.entity.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
	JavaMailSender javaMailSender;

	public void envoyer(Validation validation) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom("no-reply@ibradi.dev");
		message.setTo(validation.getUtilisateur().getEmail());
		message.setSubject("Activation de compte");

		String messageText = String.format("""
				Bonjour %s,<br/>
				votre code d'activation est: <b>%s<b/><br/>
				NB: Ce code expire dans 10 minutes.
				""", validation.getUtilisateur().getNom(), validation.getCode());
		message.setText(messageText);
 
		javaMailSender.send(message);
	}
}