package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarSeExisteConflitoReserva implements IValidatorReserva {
    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public void validar(ReservaDTO dto) {
        boolean existeConflito = reservaRepository.existeConflitoDeHorario(
                dto.equipamentoId(), dto.dataPrevistaRetirada(), dto.dataPrevistaDevolucao());

        if (existeConflito) {
            throw new ValidacaoException("Este equipamento já está reservado neste período.");
        }
    }
}
