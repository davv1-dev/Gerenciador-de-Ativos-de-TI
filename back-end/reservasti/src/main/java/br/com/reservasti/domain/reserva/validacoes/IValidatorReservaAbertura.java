package br.com.reservasti.domain.reserva.validacoes;

import org.springframework.stereotype.Component;

@Component
public interface IValidatorReservaAbertura {
    void validar(ReservaContext context);
}
