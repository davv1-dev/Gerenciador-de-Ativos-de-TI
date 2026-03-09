package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarConflitoDeEmail implements IValidatorFuncionario{
    @Autowired
    private FuncionarioRepository repository;

    @Override
    public void validar(FuncionarioContext context) {
        if(repository.existsByEmail(context.dto().email())){
            throw new ConcorrenciaException("Email ja cadastrado");
        }
        if (context.dtoDadosN()!=null){
            if (repository.existsByCpf(context.dtoDadosN().email())){
                throw new ConcorrenciaException("Email ja cadastrastrado");
            }
        }
    }
}
