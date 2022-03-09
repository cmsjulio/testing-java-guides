package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Aluno;
import net.javaguides.springboot.repository.AlunoRepository;
import net.javaguides.springboot.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlunoServiceImpl implements AlunoService {

  @Autowired
  private AlunoRepository alunoRepository;

  @Override
  public Aluno salvarAluno(Aluno aluno) {

    // adicionando exceção para quando aluno já existe.
    Optional<Aluno> alunoSalvo = alunoRepository.findByEmail(aluno.getEmail());
    if(alunoSalvo.isPresent()){
      throw new ResourceNotFoundException("Já há um aluno registrado com o email: " + aluno.getEmail());
    }

    return alunoRepository.save(aluno);
  }
}
