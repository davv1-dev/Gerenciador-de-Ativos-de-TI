package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCpf implements IValidatorFuncionario {
    @Autowired
    private FuncionarioRepository repository;
    @Override
    public void validar(FuncionarioContext context) {
        if (repository.existsByCpf(context.dto().cpf())){
            throw new ConcorrenciaException("Cpf ja cadastrastrado");
        }
    }
}
