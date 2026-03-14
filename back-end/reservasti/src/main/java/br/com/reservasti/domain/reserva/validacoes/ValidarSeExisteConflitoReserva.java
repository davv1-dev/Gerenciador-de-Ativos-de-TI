package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarSeExisteConflitoReserva implements IValidatorReservaAbertura {
    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public void validar(ReservaContext context) {
        boolean existeConflito = reservaRepository.existeConflitoDeHorario(
                context.dto().equipamentoId(), context.dto().dataPrevistaRetirada(), context.dto().dataPrevistaDevolucao());

        if (existeConflito) {
            throw new ValidacaoException("Este equipamento já está reservado neste período.");
        }
    }
}
