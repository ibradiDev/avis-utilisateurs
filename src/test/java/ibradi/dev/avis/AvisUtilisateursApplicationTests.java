package ibradi.dev.avis;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// Utilisation de Testcontainers pour mes tests

@AutoConfigureMockMvc // Pour les requêtes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Utiliser un PORT aléatoire pour mes tests
class AvisUtilisateursApplicationTests {

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0-bullseye");

	@Autowired
	MockMvc mockMvc;

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	//	Récuperer les variables d'environnement (sinon de config dans le application-properties)
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry register) {
		register.add("spring.datasource.url", postgres::getJdbcUrl);
		register.add("spring.datasource.username", postgres::getUsername);
		register.add("spring.datasource.password", postgres::getPassword);
	}

	@Test
	void testSearch() throws Exception {
		mockMvc.perform(get("/avis"))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andExpect(content().string(containsString("Ibradi")));
	}

	@Test
	void testCreate() throws Exception {
		String jsonContent = "{\"nom\": \"Test\", \"description\": \"Test Ibradi\"}";

		mockMvc.perform(post("/avis").contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isCreated()).andDo(print());
	}


}