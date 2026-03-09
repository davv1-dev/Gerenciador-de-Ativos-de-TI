package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidarData implements IValidatorReserva {
    @Override
    public void validar(ReservaDTO dto) {
        if (dto.dataPrevistaDevolucao().isBefore(dto.dataPrevistaRetirada())) {
            throw new ValidacaoException("A data de devolução não pode ser anterior à retirada.");
        }
    }
}
