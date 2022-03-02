package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AlunoRepository extends JpaRepository<Aluno, Long> {

  Optional<Aluno> findByEmail(String email);

  // definir query personalizada utilizando JPQL com index params
  @Query("select e from Aluno e where e.firstName = ?1 and e.lastName = ?2")
  Aluno findByJPQL(String firstName, String lastName);

  // definir query personalizada utilizando JPQL com name params
  @Query("select e from Aluno e where e.firstName =:firstName and e.lastName =:lastName")
  Aluno findByJPQLNameParams(@Param("firstName") String firstName,@Param("lastName") String lastName);

  // definir query personalizada utilizando parâmetros nativos SQL
  @Query(value = "select * from alunos e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
  Aluno findByNativeSQL(String firstName, String lastName);

  // definir query personalizada utilizando named params nativos SQL
  @Query(value = "select * from alunos e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
  Aluno findByNativeSQLNamed(@Param("firstName") String firstName,@Param("lastName") String lastName);

}
