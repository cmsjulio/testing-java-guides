package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.service.AlunoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

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

  @DisplayName("Controller Unit teste do endpoint criarAluno")
  @Test // pra que o JUnit detecte este método como um caso de teste JUnit.
  public void dadoObjetoAluno_quandoCriarAluno_entaoRetornarAlunoSalvo() throws Exception { //exceção do mockMvc.perform

    // DADO - pré-condição ou setup
    Aluno aluno = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();

    // precisamos mockar o método do Service envolvido no controller
    // analisando o método criarAluno no controller, vemos que este chama o método alunoService.salvarAluno
    // este processo é chamado stubbing.
    // usamos o BDDMockito para tal.

    given(alunoService.salvarAluno(any(Aluno.class))).willAnswer((invocation) -> invocation.getArgument(0)); //retorna o argumento passado (posição 0, único arg.)

    // QUANDO - ação ou comportamento a ser testado
    // para fazermos a call do API REST, usamos o mockMvc
    ResultActions response = mockMvc.perform(post("http://localhost:8080/api/alunos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(aluno)));

    // ENTÃO - verficar o resultado ou saída utilizando assert.
    response.andDo(print()) // para imprimir o output do test (request e o response)
      .andExpect(status().isCreated()) //chacando se HTTP status é o CREATED
      .andExpect(jsonPath("$.firstName", is(aluno.getFirstName()))) //checando se saída do json bate com aluno.firstname
      .andExpect(jsonPath("$.lastName", is(aluno.getLastName()))).andExpect(jsonPath("$.email", is(aluno.getEmail())));

  }

  // Controller Unit teste do endpoint listarAlunos
  @DisplayName("Controller Unit teste do endpoint listarAlunos")
  @Test
  public void dadoObjetosAluno_quandoListarAlunos_entaoRetornarListaDeAlunos() throws Exception {

    // DADO: pré-condição ou setup
    // inserindo lista de alunos
    Aluno aluno1 = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();

    Aluno aluno2 = Aluno.builder().firstName("Juliana").lastName("Silva").email("js@gmail.com").build();

    List<Aluno> listaDeAlunos = new ArrayList<>();
    listaDeAlunos.add(aluno1);
    listaDeAlunos.add(aluno2);

    //stubbing
    given(alunoService.obterAlunos()).willReturn(listaDeAlunos);


    // QUANDO: ação ou comportamento a ser testado
    // usando o mockMvc para chamar o endpoint
    ResultActions response = mockMvc.perform(get("http://localhost:8080/api/alunos"));


    // ENTÃO: verificação das saídas
    response.andDo(print()).andExpect(status().isOk()) //pra verificar se o status do response é OK
      .andExpect(jsonPath("$.size()", is(listaDeAlunos.size()))); //pra verificar se o size do json de saída $.size é igual ao da saída esperada (listaDeAlunos)

  }

  // Controller Unit teste do endpoint obterAlunoPorId(Long id)  - cenário positivo (id válido)
  @DisplayName("Controller Unit teste do endpoint obterAlunoPorId(Long id) - cenário positivo")
  @Test
  public void dadoAlunoId_quandoObterAlunoPorId_entaoRetornarObjetoAluno() throws Exception{

    // DADO: pré-condição ou setup
    Aluno aluno = Aluno.builder().id(1L).firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();

    // stubbing - atenção ao Optional.of(aluno)
    given(alunoService.obterAlunoPorId(aluno.getId())).willReturn(Optional.of(aluno));


    // QUANDO: ação ou comportamento a ser testado
    ResultActions response = mockMvc.perform(get("http://localhost:8080/api/alunos/{id}", aluno.getId()));


    // ENTÃO: verificação das saídas
    response
      .andDo(print()) // imprimindo saída
      .andExpect(status().isOk()) // checando status 200 da saída
      .andExpect(jsonPath("$.firstName", is(aluno.getFirstName())))
      .andExpect(jsonPath("$.lastName", is(aluno.getLastName())))
      .andExpect(jsonPath("$.email", is(aluno.getEmail())));


  }

  // Controller Unit teste do endpoint obterAlunoPorId(Long id)  - cenário negativo (id inválido)
  @DisplayName("Controller Unit teste do endpoint obterAlunoPorId(Long id) - cenário negativo")
  @Test
  public void dadoAlunoIdInvalido_quandoObterAlunoPorId_entaoRetornarVazio() throws Exception{

    // DADO: pré-condição ou setup
    Aluno aluno = Aluno.builder().id(1L).firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();

    // stubbing
    given(alunoService.obterAlunoPorId(aluno.getId())).willReturn(Optional.empty());


    // QUANDO: ação ou comportamento a ser testado
    ResultActions response = mockMvc.perform(get("http://localhost:8080/api/alunos/{id}", aluno.getId()));


    // ENTÃO: verificação das saídas
    response
      .andDo(print()) // imprimindo saída
      .andExpect(status().isNotFound()); // checando status 404 da saída

  }

}
