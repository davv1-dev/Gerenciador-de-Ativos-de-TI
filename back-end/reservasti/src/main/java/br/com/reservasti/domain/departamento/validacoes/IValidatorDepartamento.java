package br.com.reservasti.domain.departamento.validacoes;

import br.com.reservasti.domain.departamento.dto.DepartamentoDTO;
import org.springframework.stereotype.Component;

@Component
public interface IValidatorDepartamento {
    void validar(DepartamentoDTO dto);
}
