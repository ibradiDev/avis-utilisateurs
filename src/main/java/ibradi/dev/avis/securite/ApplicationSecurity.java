package ibradi.dev.avis.securite;

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

	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)
		                   .authorizeHttpRequests(authorize -> authorize
				                   // Autoriser toutes operation sur la route "/avis" (par defaut)
				                   .requestMatchers("/avis").permitAll()
				                   // Bloquer tte req non authentifiee (par defaut)
				                   .anyRequest().authenticated())
		                   // Gestion de session
		                   .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		                   // S'authentifier avec les config de basics de base
		                   .httpBasic(Customizer.withDefaults())
		                   // Construire le syst de securite
		                   .build();
	}
}