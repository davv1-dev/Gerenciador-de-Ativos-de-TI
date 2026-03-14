package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import org.springframework.stereotype.Component;

@Component
public interface IValidatorReservaDevolucao {
    void validar(ReservaContext context);
}
