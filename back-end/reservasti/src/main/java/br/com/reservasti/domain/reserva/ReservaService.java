package br.com.reservasti.domain.reserva;

import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import br.com.reservasti.domain.reserva.validacoes.IValidatorReserva;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private List<IValidatorReserva> validadores;

    @Transactional
    public ReservaRetornoDTO solicitarReserva(ReservaDTO dto) {

        validadores.forEach(v-> v.validar(dto));

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new IdNaoEncontradoException("Funcionário não encontrado."));

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado."));

        Reserva reserva = new Reserva(funcionario, equipamento, dto.dataPrevistaRetirada(), dto.dataPrevistaDevolucao());
        reservaRepository.save(reserva);

        return new ReservaRetornoDTO(reserva);
    }
    @Transactional
    public void retirarEquipamento(Long id){
       Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(()-> new IdNaoEncontradoException("Equipamento não encontrado"));
        equipamento.setStatus(StatusEquipamento.EM_USO);
        equipamentoRepository.save(equipamento);
    }

    @Transactional
    public void devolverEquipamento(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IdNaoEncontradoException("Reserva não encontrada."));

        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalStateException("Esta reserva já foi concluída.");
        }

        reserva.finalizarReserva();

        reservaRepository.save(reserva);
    }
    @Transactional
    public void cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IdNaoEncontradoException("Reserva não encontrada com o ID: " + idReserva));

        if (reserva.getStatus() == StatusReserva.CANCELADA || reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalStateException("Esta reserva não pode ser cancelada no status atual: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReserva.CANCELADA);


    }
}
