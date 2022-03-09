package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.repository.AlunoRepository;
import net.javaguides.springboot.service.AlunoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoServiceImpl implements AlunoService {

  // @Autowired //depois de criado o construtor, o @Autowired torna-se desnecessário.
  private AlunoRepository alunoRepository;

  public AlunoServiceImpl(AlunoRepository alunoRepository) {
    this.alunoRepository = alunoRepository;
  }
  // AlunoService utiliza AlunoRepository, para que possamos testar AlunoService impedindo a possibilidade
  //  de erros no AlunoRepository, precisamos utilizar um mock (imitação) do AlunoRepository.
  // O framework mockito é responsável por implementar este mock.

  /*

  A forma mais simples de mockar um objeto com mockito é utilizando o método estático Mockito mock():

  Também podemos utilizar a anotação @Mock

  Outra anotação importante do Mockito é a @InjectMocks, utilizada para injetar um objeto mocado dentro de
    um outro objeto mockado. Ex.:

        @ExtendWith(MockitoExtension.class)
        public class AlunoServiceTest{

          @Mock
          private AlunoRepository alunoRepository;

          @InjectMocks //primeiro mocka o Service, depois injeta o mock do Repository no mock do Service.
          private AlunoServiceImpl alunoService;

          ...
          }

  A classe BDDMockito facilita a implementação de testes com mockito utilizando o formato BDD (Behavior Driven
    Development -- estrutura dado_quando_então/given_when_then)

  Um dos métodos principais é o given(x).willReturn(y);

   */

  @Override
  public Aluno salvarAluno(Aluno aluno) {

    // adicionando exceção para quando aluno já existe.
    Optional<Aluno> alunoSalvo = alunoRepository.findByEmail(aluno.getEmail());
    if(alunoSalvo.isPresent()){
      throw new ResourceNotFoundException("Já há um aluno registrado com o email: " + aluno.getEmail());
    }

    return alunoRepository.save(aluno);
  }

  // implementação do método obterAlunos()
  @Override
  public List<Aluno> obterAlunos() {
    return alunoRepository.findAll();
  }

  // implementação do método criado na interface AlunoService
  @Override
  public Optional<Aluno> obterAlunoPorId(Long id) {
    return alunoRepository.findById(id);
  }

  // implementação simples do método atualizarAluno
  @Override
  public Aluno atualizarAluno(Aluno alunoAtualizado) {
    return alunoRepository.save(alunoAtualizado);
  }

  @Override
  public void deletarAluno(Long id) {
    alunoRepository.deleteById(id);
  }
}
