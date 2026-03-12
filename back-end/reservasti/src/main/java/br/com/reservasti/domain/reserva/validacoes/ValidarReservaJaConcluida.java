package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarReservaConcluida implements IValidatorReservaAbertura {
    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public void validar(ReservaContext context) {
    Reserva reserva = reservaRepository.getReferenceById(context.idReserva());
        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new ValidacaoException("Esta reserva já foi concluída.");
        }

    }
}
