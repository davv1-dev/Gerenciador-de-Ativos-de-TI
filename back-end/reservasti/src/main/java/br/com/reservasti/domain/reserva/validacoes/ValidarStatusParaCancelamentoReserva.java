package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarStatusParaCancelamentoReserva implements IValidatorReservaDevolucao {
    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public void validar(ReservaContext context) {
        Reserva reserva = reservaRepository.getReferenceById(context.idReserva());
        if (reserva.getStatus() == StatusReserva.CANCELADA || reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new ValidacaoException("Esta reserva não pode ser cancelada no status atual: " + reserva.getStatus());
        }

    }

}
