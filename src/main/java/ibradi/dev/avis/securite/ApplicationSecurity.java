package ibradi.dev.avis.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ApplicationSecurity {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)
		                   .authorizeHttpRequests(authorize -> authorize
				                   // Autoriser toutes les opérations sur la route "/avis"
				                   .requestMatchers("/avis").permitAll()
				                   // Bloquer toutes les requêtes non authentifiées
				                   .anyRequest().authenticated())
		                   // Gestion de session
		                   .sessionManagement(httpSecuritySessionManagementConfigurer ->
				                   httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		                   // S'authentifier avec les configurations de base
		                   .httpBasic(Customizer.withDefaults())
		                   // Construire le système de sécurité
		                   .build();
	}
}