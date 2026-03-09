package br.com.reservasti.domain.helpdesk.validacoes;

import org.springframework.stereotype.Component;

@Component
public interface IValidatorChamado {
    void validar(ChamadoContext context);
}
