package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidarRetirada implements IValidatorReserva{

    @Override
    public void validar(ReservaDTO dto) {
        if (dto.dataPrevistaDevolucao().isBefore(dto.dataPrevistaRetirada())) {
            throw new IllegalArgumentException("A data de devolução não pode ser anterior à retirada.");
        }
    }
}
