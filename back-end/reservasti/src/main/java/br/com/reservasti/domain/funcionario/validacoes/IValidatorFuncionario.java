package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import org.springframework.stereotype.Component;

@Component
public interface IValidatorFuncionario {
    void validar(FuncionarioDTO dto);
}
