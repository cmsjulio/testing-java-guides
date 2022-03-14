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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

  // utilizamos o mesmo código do Controller Unit teste, com o stubbing removido.
  // Controller Integration teste do endpoint listarAlunos
  @DisplayName("Controller Integration teste do endpoint listarAlunos")
  @Test
  public void dadoObjetosAluno_quandoListarAlunos_entaoRetornarListaDeAlunos() throws Exception {

    // DADO: pré-condição ou setup
    // inserindo lista de alunos
    Aluno aluno1 = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();
    Aluno aluno2 = Aluno.builder().firstName("Juliana").lastName("Silva").email("js@gmail.com").build();

    List<Aluno> listaDeAlunos = new ArrayList<>();
    listaDeAlunos.add(aluno1);
    listaDeAlunos.add(aluno2);

    // precisamos salvar em persitência o que antes era mocado.
    alunoRepository.saveAll(listaDeAlunos);

    // QUANDO: ação ou comportamento a ser testado
    // usando o mockMvc para chamar o endpoint
    ResultActions response = mockMvc.perform(get("http://localhost:8080/api/alunos"));


    // ENTÃO: verificação das saídas
    response.andDo(print()).andExpect(status().isOk()) //pra verificar se o status do response é OK
      .andExpect(jsonPath("$.size()", is(listaDeAlunos.size()))); //pra verificar se o size do json de saída $.size é igual ao da saída esperada (listaDeAlunos)

  }

  // mesmo código do Controller Unit teste, mas: sem stubbin e com persistência.
  // Controller Integration teste do endpoint obterAlunoPorId(Long id) - cenário positivo (id válido)
  @DisplayName("Controller Integration teste do endpoint obterAlunoPorId(Long id) - cenário positivo")
  @Test
  public void dadoAlunoId_quandoObterAlunoPorId_entaoRetornarObjetoAluno() throws Exception {

    // DADO: pré-condição ou setup
    Aluno aluno = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();

    // gravando na base:
    alunoRepository.save(aluno);


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

  // mesmo caso do Controller Unit teste, mas com armazenamento em base.
  // Controller Integration teste do endpoint obterAlunoPorId(Long id)  - cenário negativo (id inválido)
  @DisplayName("Controller Integration teste do endpoint obterAlunoPorId(Long id) - cenário negativo")
  @Test
  public void dadoAlunoIdInvalido_quandoObterAlunoPorId_entaoRetornarVazio() throws Exception {

    // DADO: pré-condição ou setup
    Aluno aluno = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();
    alunoRepository.save(aluno);


    // QUANDO: ação ou comportamento a ser testado
    ResultActions response = mockMvc.perform(get("http://localhost:8080/api/alunos/{id}", aluno.getId()+1L)); // id de aluno inexistente


    // ENTÃO: verificação das saídas
    response
      .andDo(print()) // imprimindo saída
      .andExpect(status().isNotFound()); // checando status 404 da saída

  }

  // mesmo caso do Controller Unit teste, mas sem stubbing/mock
  // Controller Integration teste do endpoint updateAluno(Long) - cenário positivo
  @DisplayName("Controller Integration teste do endpoint updateAluno(Long) - cenário positivo")
  @Test
  public void dadoAlunoAtualizado_quandoUpdateAluno_entaoRetornarObjetoAlunoAtualizado() throws Exception { // exception do writeValueAsString

    // DADO: pré-condição ou setup
    Aluno alunoSalvo = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();
    Aluno alunoAtualizado = Aluno.builder().firstName("Cézar").lastName("Mendes").email("jjj@gmail.com").build();

    alunoRepository.save(alunoSalvo);


    // QUANDO: ação ou comportamento a ser testado
    ResultActions response = mockMvc.perform(put("http://localhost:8080/api/alunos/{id}", alunoSalvo.getId())
      .contentType(MediaType.APPLICATION_JSON)  // dizendo que o request type é json
      .content(objectMapper.writeValueAsString(alunoAtualizado))); // content do request é alunoAtualizado

    // ENTÃO: verificação das saídas
    response.andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.firstName", is(alunoAtualizado.getFirstName())))
      .andExpect(jsonPath("$.lastName", is(alunoAtualizado.getLastName())))
      .andExpect(jsonPath("$.email", is(alunoAtualizado.getEmail())));

  }

  // Controller Integration teste do endpoint updateAluno(Long) - cenário negativo
  @DisplayName("Controller Integration teste do endpoint updateAluno(Long) - cenário negativo")
  @Test
  public void dadoAlunoAtualizado_quandoUpdateAluno_entaoRetornar404() throws Exception { // exception do writeValueAsString

    // DADO: pré-condição ou setup
    Aluno alunoSalvo = Aluno.builder().firstName("Julio").lastName("Silva").email("cms.julio1@gmail.com").build();
    Aluno alunoAtualizado = Aluno.builder().firstName("Cézar").lastName("Mendes").email("jjj@gmail.com").build();

    alunoRepository.save(alunoSalvo);


    // QUANDO: ação ou comportamento a ser testado
    ResultActions response = mockMvc.perform(put("http://localhost:8080/api/alunos/{id}", alunoSalvo.getId()+1L)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(alunoAtualizado))); // lança exceção

    // ENTÃO: verificação das saídas
    response.andDo(print())
      .andExpect(status().isNotFound());


  }



}
