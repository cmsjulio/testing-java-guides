package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoService {
  Aluno salvarAluno(Aluno aluno);
  List<Aluno> obterAlunos(); // novo método criado na interface
  Optional<Aluno> obterAlunoPorId(Long id); // novo método, para obter aluno por Id.
}
