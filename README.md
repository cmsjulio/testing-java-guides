## Do curso de Testing da Udemy
### ministrado por Java Guides

### Testes de integração
Os testes de integração são focados em, como o nome sugere, testar a relação entre diferentes camadas da aplicação que sejam dependentes entre si. Qual seja: não se utiliza de Mock em tais testes.

Os Unit testes são utilizados para testar as camadas uma a uma, o Integration Test é utilizado para testar o fluxo completo da aplicação.

A anotação utilizada em testes de integração é a *@SpringBootTest*.

<img src="img.png">

Os testes são utilizados conforme o FEATURE que se deseja testar. Cada feature envolvendo a integração de componentes múltimos: E.g.: <br> 
**Gerenciamento de Aluno**: AlunoRepository, AlunoService, AlunoController. <br>
**Login**: LoginRepository, LoginService, LoginController. <br>
**Gerenciamento de Usuário**: UsuarioRepository, UsuarioService, UsuarioController.




