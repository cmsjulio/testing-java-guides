package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.repository.AlunoRepository;
import net.javaguides.springboot.service.impl.AlunoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

public class AlunoServiceTests {

  private AlunoRepository alunoRepository;
  private AlunoService alunoService;

  // criamos o mock no setup()
  @BeforeEach
  public void setup() {

    // como utilizamos o método estático mock() para mockar uma interface ou classe.
    alunoRepository = Mockito.mock(AlunoRepository.class);

    // precisamos alterar o código e criar um construtor em AlunoServiceImpl para podermos
    // trabalhar desta forma.
    alunoService = new AlunoServiceImpl(alunoRepository);

  }

  // Teste JUnit para método salvarAluno
  @DisplayName("Test JUnit para método salvarAluno")
  @Test
  public void dadoObjetoAluno_quandoSalvarAluno_entaoRetornarObjetoAluno() {

    // DADO: pré-condição ou setup

    Aluno aluno  = Aluno.builder()
      .id(1L)
      .firstName("Julio")
      .lastName("Mendes")
      .email("julio@hotmail.com")
      .build();

    // se observarmos a classe AlunoServiceImpl, vemos que o método salvarAluno utiliza os métodos
    // alunoRepository.findByEmail e alunoRepository.save.
    // precisamos fornecer um stub in para ambos os dois métodos: findByEmail e save.

    // findByEmail:
    BDDMockito.given(alunoRepository.findByEmail(aluno.getEmail()))
      .willReturn(Optional.empty());
    // qual seja: sempre que chamar findByEmail(aluno.getEmail()), retornar Optional.empty().

    // save:
    BDDMockito.given(alunoRepository.save(aluno))
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
}
