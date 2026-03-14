package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidarData implements IValidatorReservaAbertura {

    @Override
    public void validar(ReservaContext context) {
        if (context.dto().dataPrevistaDevolucao().isBefore(context.dto().dataPrevistaRetirada())) {
                throw new ValidacaoException("A data de devolução não pode ser anterior à retirada.");
        }
    }
}
