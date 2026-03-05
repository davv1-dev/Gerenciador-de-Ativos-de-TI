package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidarSeExisteConflitoReserva implements IValidatorReserva {
    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public void validar(ReservaDTO dto) {
        boolean existeConflito = reservaRepository.existeConflitoDeHorario(
                dto.equipamentoId(), dto.dataPrevistaRetirada(), dto.dataPrevistaDevolucao());

        if (existeConflito) {
            throw new IllegalStateException("Este equipamento já está reservado neste período.");
        }
    }
}
