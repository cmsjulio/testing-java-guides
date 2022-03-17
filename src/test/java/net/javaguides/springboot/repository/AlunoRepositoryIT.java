package net.javaguides.springboot.repository;

import net.javaguides.springboot.integration.AbstractContainerBaseTest;
import net.javaguides.springboot.model.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // utiliza, por padrão, base H2 em memória; para o IT, precisamos desabilitar
// para apontarmos para a base real, de produção, utilizamos a @AutoConfigureTestDatabase, conforme abaixo:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AlunoRepositoryIT extends AbstractContainerBaseTest { // com esse extends, utilizamos o singleton container.

  @Autowired
  private AlunoRepository alunoRepository;

  private Aluno aluno;

  //código que roda antes de todos @Test; inserir no setup() os passos comuns a todos os testes.
  @BeforeEach
  public void setup(){
    aluno = Aluno.builder()
      .firstName("Júlio")
      .lastName("Silva")
      .email("cms.julio1@gmail.com")
      .build();

    System.out.println("Username: " + MY_SQL_CONTAINER.getUsername());
    System.out.println("Password: " + MY_SQL_CONTAINER.getPassword());
    System.out.println("JDBC url: " + MY_SQL_CONTAINER.getJdbcUrl());
    System.out.println("Database: " + MY_SQL_CONTAINER.getDatabaseName());

  }

  // Teste de Integração para operação de salvar aluno.
  @DisplayName("Teste de Integração para operação de salvar aluno.")
  @Test // do JUnit framework.
  public void dadoObjetoAluno_quandoSalvar_entaoRetornarAlunoSalvo() {

    // dado: pré-condição ou setup

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.save(aluno);

    // entao: verificação das saídas
    assertThat(alunoSalvo).isNotNull(); // importar do asserj.core
    assertThat(alunoSalvo.getId()).isGreaterThan(0);

  }

  // Teste de Integração para operação de GET lista de alunos
  @DisplayName("Teste de Integração para operação de GET lista de alunos")
  @Test
  public void dadoObjetosAlunos_quandoFindAll_entaoRetornarListaDeAlunos() {

    // dado: pré-condição ou setup
    Aluno aluno1 = Aluno.builder()
      .firstName("Júlio")
      .lastName("Silva")
      .email("cms.julio1@gmail.com")
      .build();

    Aluno aluno2 = Aluno.builder()
      .firstName("João")
      .lastName("Pedro")
      .email("jp1@gmail.com")
      .build();

    alunoRepository.save(aluno1);
    alunoRepository.save(aluno2);

    // quando: ação ou comportamento a ser testado
    List<Aluno> listaDeAlunos = alunoRepository.findAll();

    // entao: verificação das saídas
    assertThat(listaDeAlunos).isNotNull();
    assertThat(listaDeAlunos.size()).isEqualTo(2);

  }

  // Teste de Integração para encontrar aluno por Id
  @DisplayName("Teste de Integração para encontrar aluno por Id")
  @Test
  public void dadoObjetoAluno_quandoFindById_entaoRetornarAlunoDoBancoDeDados() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    Aluno alunoEncontrado = alunoRepository.findById(aluno.getId()).get();
    // precisa do get pois findBYId retorna Optional.

    // entao: verificação das saídas
    assertThat(alunoEncontrado).isNotNull();

  }

  // Teste de Integração para encontrar aluno por email
  @DisplayName("Teste de Integração para encontrar aluno por email")
  @Test
  public void dadoEmailDoObjetoAluno_quandoFindByEmail_entaoRetornarObjetoAluno() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    Aluno alunoEncontradoPorEmail = alunoRepository.findByEmail(aluno.getEmail()).get();

    // entao: verificação das saídas
    assertThat(alunoEncontradoPorEmail).isNotNull();

  }

  // Teste de Integração para operação de atualizar aluno
  @DisplayName("Teste de Integração para operação de atualizar aluno")
  @Test
  public void dadoObjetoAluno_quandoAtualizarAluno_entaoRetornarAlunoAtualizado() {

    // dado: pré-condição ou setup
//    Aluno aluno1 = Aluno.builder()
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.findById(aluno.getId()).get();
    alunoSalvo.setEmail("jjj@gmail.com");
    alunoSalvo.setFirstName("Jota");
    Aluno alunoAtualizado = alunoRepository.save(alunoSalvo);

    // entao: verificação das saídas
    assertThat(alunoAtualizado.getEmail()).isEqualTo("jjj@gmail.com");
    assertThat(alunoAtualizado.getFirstName()).isEqualTo("Jota");

  }

  // Teste de Integração para operação de deletar aluno
  @DisplayName("Teste de Integração para operação de deletar aluno")
  @Test
  public void dadoObjetoAluno_quandoDeletarAluno_entaoRemoverAluno() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    alunoRepository.deleteById(aluno.getId());
    Optional<Aluno> alunoOptional = alunoRepository.findById(aluno.getId());

    // entao: verificação das saídas
    assertThat(alunoOptional).isEmpty();

  }

  // Teste de Integração para query personalizada utilizando index JPQL
  @DisplayName("Teste de Integração para query personalizada utilizando index JPQL")
  @Test
  public void dadoFirstNameAndLastName_quandoFindByJPQL_entaoRetornarObjetoAluno() {

    // dado: pré-condição ou setup
//    Aluno aluno1 = Aluno.builder()
    alunoRepository.save(aluno);

    String firstName = "Júlio";
    String lastName = "Silva";

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.findByJPQL(firstName, lastName);

    // entao: verificação das saídas
    assertThat(alunoSalvo).isNotNull();

  }


  // Teste de Integração para query personalizada utilizando named params JPQL
  @DisplayName("Teste de Integração para query personalizada utilizando named params JPQL")
  @Test
  public void dadoFirstNameAndLastName_quandoFindByJPQLNamedParams_entaoRetornarObjetoAluno() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    String firstName = "Júlio";
    String lastName = "Silva";

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.findByJPQLNameParams(firstName, lastName);

    // entao: verificação das saídas
    assertThat(alunoSalvo).isNotNull();

  }

  // Teste de Integração para query personalizada utilizando index SQL nativo
  @DisplayName("Teste de Integração para query personalizada utilizando index SQL nativo")
  @Test
  public void dadoFirstNameAndLastName_quandoFindByNativeSQL_entaoRetornaObjetoAluno() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.findByNativeSQL(aluno.getFirstName(), aluno.getLastName());

    // entao: verificação das saídas
    assertThat(alunoSalvo).isNotNull();

  }

  // Teste de Integração para query personalizada utilizando named params SQL nativo
  @DisplayName("Teste de Integração para query personalizada utilizando named params SQL nativo")
  @Test
  public void dadoFirstNameAndLastName_quandoFindByNativeSQLNamedParams_entaoRetornaObjetoAluno() {

    // dado: pré-condição ou setup
    alunoRepository.save(aluno);

    // quando: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoRepository.findByNativeSQLNamed(aluno.getFirstName(), aluno.getLastName());

    // entao: verificação das saídas
    assertThat(alunoSalvo).isNotNull();

  }

}
