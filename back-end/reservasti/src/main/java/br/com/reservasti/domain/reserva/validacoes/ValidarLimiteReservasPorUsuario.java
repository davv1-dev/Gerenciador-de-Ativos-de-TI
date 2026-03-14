package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidarLimiteReservasPorUsuario implements IValidatorReservaAbertura {
    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void validar(ReservaContext context) {

        List<StatusReserva> statusAtivos = List.of(StatusReserva.AGENDADA, StatusReserva.ATIVA);

        long quantidadeEquipamentosReservados = reservaRepository.countByFuncionarioIdAndStatusIn(
                context.dto().funcionarioId(),
                statusAtivos
        );

        if (quantidadeEquipamentosReservados >= 2) {
            throw new ValidacaoException("Limite atingido! O funcionário já possui 2 equipamentos reservados ou em uso.");
        }
    }
}
