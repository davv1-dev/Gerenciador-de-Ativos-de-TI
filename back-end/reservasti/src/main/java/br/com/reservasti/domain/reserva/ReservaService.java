package br.com.reservasti.domain.reserva;

import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import br.com.reservasti.domain.reserva.validacoes.IValidatorReserva;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private IValidatorReserva validadores;

    @Transactional
    public ReservaRetornoDTO solicitarReserva(ReservaDTO dto) {

        validadores.validar(dto);

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado."));

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        Reserva reserva = new Reserva(funcionario, equipamento, dto.dataPrevistaRetirada(), dto.dataPrevistaDevolucao());
        reservaRepository.save(reserva);

        return new ReservaRetornoDTO(reserva);
    }

    @Transactional
    public void devolverEquipamento(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada."));

        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalStateException("Esta reserva já foi concluída.");
        }

        reserva.finalizarReserva();
    }
}
