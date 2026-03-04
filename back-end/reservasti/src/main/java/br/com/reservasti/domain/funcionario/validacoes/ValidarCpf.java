package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarCpf implements IValidatorFuncionario {
    @Autowired
    private FuncionarioRepository repository;
    @Override
    public void validar(FuncionarioDTO dto) {
        if (repository.existsByCpf(dto.cpf())){
            throw new RuntimeException("Cpf ja cadastrastrado");
        }
    }
}
