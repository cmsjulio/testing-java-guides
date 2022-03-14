package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //para testes, necessário definir webEnv como RANDOM
@AutoConfigureMockMvc //utilizamos o MockMvc para chamar as API Rest
public class AlunoControllerITests {

  @Autowired
  private MockMvc mockMvc; //para realizar chamadas HTTP utilizando o método perform()

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup (){
    alunoRepository.deleteAll();
    // limpando a base de dados antes de todos os testes a serem executados.
  }
}
