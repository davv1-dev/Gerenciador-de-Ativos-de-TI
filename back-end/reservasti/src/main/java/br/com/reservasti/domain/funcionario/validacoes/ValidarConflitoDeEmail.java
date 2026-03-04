package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarConflitoDeEmail implements IValidatorFuncionario{
    @Autowired
    private FuncionarioRepository repository;

    @Override
    public void validar(FuncionarioDTO dto) {
        if(repository.existsByEmail(dto.email())){
            throw new RuntimeException("Email ja cadastrado");
        }
    }
    public void validaEmailAtualizacao(FuncionarioAtualizacaoDTO dadosnovos){
        if (repository.existsByCpf(dadosnovos.email())){
            throw new RuntimeException("Email ja cadastrastrado");
        }
    }
}
