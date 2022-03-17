package net.javaguides.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {

  protected static final MySQLContainer MY_SQL_CONTAINER;

  // utilizando o bloco estático para criar/iniciar o objeto
  static {
    MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
      .withUsername("testContUser")
      .withPassword("testContPasswd")
      .withDatabaseName("testContDb");

    MY_SQL_CONTAINER.start(); // para iniciar o container explicitamente
    // ao fazermos isso, não estamos mais permitindo que o Testcontainers cuide do ciclo de vida dos containers;
    // estamos iniciando manualmente o container.
    // portanto, devemos remover a anotação @Testcontainers das classes testadas.
  }

  @DynamicPropertySource
  public static void dynamicPropertySource(DynamicPropertyRegistry registry){
    registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
  }

}
