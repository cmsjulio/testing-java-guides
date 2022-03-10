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


 */

public class AlunoController {
}
