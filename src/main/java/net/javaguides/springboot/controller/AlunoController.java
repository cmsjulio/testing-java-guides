package net.javaguides.springboot.controller;

/*

  Testando componentes do controller -- intro.

  Usamos o @WebMvcTest, com o Service sendo mockado com o Mockito.

  Utilizaremos a biblitoeca Hamcrest para testar o API REST.
    A biblioteca oferece as matcher classes para assertions.
    Ela já está embutida no JUnit.

    Hamcrest fornece o método is(), utilizado para verificar o resultado real com o esperado.
    Syntax:
      assertThat(ACTUAL, is(EXPECTED));

  Também utilizaremos a biblioteca JsonPath -- um DSL Java para ler documentos JSON.

    Exemplos:

      JSON:
      {
        "firstName": "Ramesh",
        "lastName": "Fatadare"
      }

      $ - whole JSON, root member. Object or array.

      $.firstName = "Ramesh"
      $.lastName = "Fadatare"

      --

      @WebMvcTest
      Carrega apenas o controller anotado e suas dependências,
      economizando o tempo de carregar a aplicação inteira (Service e Repository).

      O Spring Boot instancia apenas a camada web, em vez da aplicação inteira.
      Quando há mais de um controlador, pode-se utilizar a anotação da forma: @WebMvcTest(HomeController.class)

      A especificação do teste por componetne torna a aplicação mais rápida.

      --

      @WebMvcTest vs @SpringBootTest
      A anotação @WebMvcTest é disponibilizada pelo Sprinb Boot, esta cria o contexto da aplicação,
      configurando apenas os Beans necessários para o teste do Spring MVC Controller.
      Os Beans do Service e Repository, por exemplo, não são carregados.

      O Spring Boot disponibiliza também a anotação @SpringBootTest, utilizada para testes de integração.
      Testes de integração envolvem todas as camadas da aplicação: Controller, Service, Repository e DB.
      A anotação @SprinbBootTest irá, portanto, carregar toda a aplicação: i.d., todos os Beans de todas as camadas.

      Controller Unit Test: @WebMvcTest
      Integration test:     @SpringBootTest



 */

import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

  private AlunoService alunoService;

  // com o construtor, não precisamos do @Autowired
  public AlunoController(AlunoService alunoService){
    this.alunoService = alunoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED) //por padrão, a resposta é 200, pra alterar, anotamos desta forma.
  public Aluno criarAluno(@RequestBody Aluno aluno){ //@RequestBody usa métodos internos para converter JSON/obj.
    return alunoService.salvarAluno(aluno);
  }

  @GetMapping
  public List<Aluno> listarAlunos() {
    return alunoService.obterAlunos();
  }

}
