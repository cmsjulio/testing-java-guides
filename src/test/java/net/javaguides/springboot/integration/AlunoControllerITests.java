package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  // Copiamos o mesmo código do Controller Unit, mas sem o stubbing dos métodos do Service.
  @DisplayName("Controller Integration teste do endpoint criarAluno(Aluno)")
  @Test // pra que o JUnit detecte este método como um caso de teste JUnit.
  public void dadoObjetoAluno_quandoCriarAluno_entaoRetornarAlunoSalvo() throws Exception { //exceção do mockMvc.perform

    // DADO - pré-condição ou setup
    Aluno aluno = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();


    // QUANDO - ação ou comportamento a ser testado
    // para fazermos a call do API REST, usamos o mockMvc
    ResultActions response = mockMvc.perform(post("http://localhost:8080/api/alunos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(aluno)));

    // ENTÃO - verficar o resultado ou saída utilizando assert.
    response.andDo(print()) // para imprimir o output do test (request e o response)
      .andExpect(status().isCreated()) //chacando se HTTP status é o CREATED
      .andExpect(jsonPath("$.firstName", is(aluno.getFirstName()))) //checando se saída do json bate com aluno.firstname
      .andExpect(jsonPath("$.lastName", is(aluno.getLastName()))).andExpect(jsonPath("$.email", is(aluno.getEmail())));

  }
}
