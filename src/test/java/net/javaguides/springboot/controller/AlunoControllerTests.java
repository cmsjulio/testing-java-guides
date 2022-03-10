package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.service.AlunoService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest // carrega apenas os Beans necessários para testar o Controller.
public class AlunoControllerTests {

  // usaremos o mockMvc para chamar os API REST.
  @Autowired
  private MockMvc mockMvc;

  // para criar um mock do Service que será adicionado ao contexto da aplicação, sendo injetado no AlunoController.
  @MockBean // do framework Spring.
  private AlunoService alunoService;

  // para serializar e desserializar objetos Java, utilizaremos a classe jackson Object Mapper.
  @Autowired
  private ObjectMapper objectMapper;

  @Test // pra que o JUnit detecte este método como um caso de teste JUnit.
  public void dadoObjetoAluno_quandoCriarAluno_entaoRetornarAlunoSalvo() throws Exception{ //exceção do object mapper

    // DADO - pré-condição ou setup
    Aluno aluno = Aluno.builder()
      .firstName("Julio")
      .lastName("Silva")
      .email("cms.julio1@gmail.com")
      .build();

    // precisamos mockar o método do Service envolvido no controller
    // analisando o método criarAluno no controller, vemos que este chama o método alunoService.salvarAluno
    // este processo é chamado stubbing.
    // usamos o BDDMockito para tal.

    BDDMockito.given(alunoService.salvarAluno(ArgumentMatchers.any(Aluno.class)))
      .willAnswer((invocation) -> invocation.getArgument(0)); //retorna o argumento passado (posição 0, único arg.)

    // QUANDO - ação ou comportamento a ser testado
    // para fazermos a call do API REST, usamos o mockMvc
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/alunos")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(aluno)));

    // ENTÃO - verficar o resultado ou saída utilizando assert.
    response.andDo(MockMvcResultHandlers.print()) // para imprimir o output do test (request e o response)
      .andExpect(MockMvcResultMatchers.status().isCreated()) //chacando se HTTP status é o CREATED
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
        CoreMatchers.is(aluno.getFirstName()))) //checando se saída do json bate com aluno.firstname
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
        CoreMatchers.is(aluno.getLastName())))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email",
        CoreMatchers.is(aluno.getEmail())));

  }
}
