package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Aluno;

import java.util.List;

public interface AlunoService {
  Aluno salvarAluno(Aluno aluno);
  List<Aluno> obterAlunos(); // novo método criado na interface
}
