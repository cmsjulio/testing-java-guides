package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.repository.AlunoRepository;
import net.javaguides.springboot.service.impl.AlunoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given; //para chamarmos o método com apenas given, como fizemos com assertThat
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // para informarmos que estamos utilizando anotações do Mockito para mockar as dependências.
public class AlunoServiceTests {

  @Mock
  private AlunoRepository alunoRepository;

  @InjectMocks // para criarmos objectos Mock que serão precisarão ser injetados dentro das classes anotadas com @Mock
  private AlunoServiceImpl alunoService;

  private Aluno aluno; // objeto que será utilizado em todos os testes, configurado em @BeforeEach setup();


  // criamos o mock no setup()
  @BeforeEach
  public void setup() {

    // como utilizamos o método estático mock() para mockar uma interface ou classe.
    // alunoRepository = Mockito.mock(AlunoRepository.class); // a anotação @Mock substitui esta linha.

    // precisamos alterar o código e criar um construtor em AlunoServiceImpl para podermos
    // trabalhar desta forma.
    // alunoService = new AlunoServiceImpl(alunoRepository); // a anotação @InjectMocks substitui esta linha

    // configuração do objeto Aluno, utilizado em todos os testes:
    aluno  = Aluno.builder()
      .id(1L)
      .firstName("Julio")
      .lastName("Mendes")
      .email("julio@hotmail.com")
      .build();
  }

  // Teste JUnit para método salvarAluno
  @DisplayName("Test JUnit para método salvarAluno")
  @Test
  public void dadoObjetoAluno_quandoSalvarAluno_entaoRetornarObjetoAluno() {

    // DADO: pré-condição ou setup

    // se observarmos a classe AlunoServiceImpl, vemos que o método salvarAluno utiliza os métodos
    // alunoRepository.findByEmail e alunoRepository.save.
    // precisamos fornecer um stub in para ambos os dois métodos: findByEmail e save.

    // findByEmail:
    given(alunoRepository.findByEmail(aluno.getEmail()))
      .willReturn(Optional.empty());
    // qual seja: sempre que chamar findByEmail(aluno.getEmail()), retornar Optional.empty().

    // save:
    given(alunoRepository.save(aluno))
      .willReturn(aluno);
    // qual seja: quando chamarmos .save, retornamos aluno;


    // para checar se os mocks estão sendo construídos
    System.out.println(alunoRepository);
    System.out.println(alunoService);

    // QUANDO: ação ou comportamento a ser testado
    Aluno alunoSalvo = alunoService.salvarAluno(aluno);

    //para observar saída do .salvarAluno
    System.out.println(alunoSalvo);
    // ENTÃO: verificação das saídas
    Assertions.assertThat(alunoSalvo).isNotNull();

  }

  // Teste JUnit para método salvarAluno
  @DisplayName("Test JUnit para método salvarAluno com thrown exception")
  @Test
  public void dadoObjetoAluno_quandoSalvarAluno_entaoThrowsException() {

    // DADO: pré-condição ou setup

    // findByEmail -- precisa retornar um aluno existente.:
    given(alunoRepository.findByEmail(aluno.getEmail()))
      .willReturn(Optional.of(aluno));
    // qual seja: sempre que chamar findByEmail(aluno.getEmail()), retornar Optional.of(aluno).

    // save:
    // given(alunoRepository.save(aluno)).willReturn(aluno); // given não utilizado aponta erro: precisa remover.
    // a exceção evita o chamamento de .save, por isso o given fica inutilizado no caso de existir saída de .findByEmail.

    // para checar se os mocks estão sendo construídos
    System.out.println(alunoRepository);
    System.out.println(alunoService);

    // QUANDO: ação ou comportamento a ser testado

    // para lidarmos com exceções nos testes:
    org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      alunoService.salvarAluno(aluno);
    });

    // ENTÃO -- precisamos checar que, após lançar exceção, o mock de alunoRepository não executa o .save(aluno)
    verify(alunoRepository, never()).save(any(Aluno.class)); // any do ArgumentMatchers

  }
}
