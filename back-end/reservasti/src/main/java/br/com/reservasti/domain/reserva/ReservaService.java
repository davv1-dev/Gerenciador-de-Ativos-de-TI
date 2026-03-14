package br.com.reservasti.domain.reserva;

import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import br.com.reservasti.domain.reserva.validacoes.IValidatorReservaAbertura;
import br.com.reservasti.domain.reserva.validacoes.IValidatorReservaDevolucao;
import br.com.reservasti.domain.reserva.validacoes.ReservaContext;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private List<IValidatorReservaAbertura> validadoresAbertura;
    @Autowired
    private List<IValidatorReservaDevolucao> validadoresDevolucao;

    @Transactional
    public ReservaRetornoDTO solicitarReserva(ReservaDTO dto) {

        validadoresAbertura.forEach(v-> v.validar(new ReservaContext(null,dto)));

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new IdNaoEncontradoException("Funcionário não encontrado."));

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado."));

        Reserva reserva = new Reserva(funcionario, equipamento, dto.dataPrevistaRetirada(), dto.dataPrevistaDevolucao());
        reservaRepository.save(reserva);

        return new ReservaRetornoDTO(reserva);
    }
    @Transactional
    public void retirarEquipamento(Long idequipamento,Long idReserva){
       Equipamento equipamento = equipamentoRepository.findById(idequipamento).orElseThrow(()-> new IdNaoEncontradoException("Equipamento não encontrado"));
       Reserva reserva = reservaRepository.findById(idReserva).orElseThrow(()-> new IdNaoEncontradoException("Reserva não encontrada"));
        reserva.iniciarReserva();
        equipamento.setStatus(StatusEquipamento.EM_USO);

        equipamentoRepository.save(equipamento);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void devolverEquipamento(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IdNaoEncontradoException("Reserva não encontrada."));
        validadoresDevolucao.forEach(v->v.validar(new ReservaContext(reservaId,null)));
        Equipamento equipamento = reserva.getEquipamento();

        equipamento.setStatus(StatusEquipamento.DISPONIVEL);
        reserva.finalizarReserva();

        reservaRepository.save(reserva);
    }
    @Transactional
    public void cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IdNaoEncontradoException("Reserva não encontrada com o ID: " + idReserva));
        validadoresDevolucao.forEach(v->v.validar(new ReservaContext(idReserva,null)));

        reserva.setStatus(StatusReserva.CANCELADA);


    }

    public Page<ReservaRetornoDTO> listarMinhasReservasAtivas(Long funcionarioId, Pageable paginacao) {

        if (!funcionarioRepository.existsById(funcionarioId)) {
            throw new IdNaoEncontradoException("Funcionário não encontrado no sistema.");
        }

        List<StatusReserva> statusAtivos = List.of(
                StatusReserva.AGENDADA,
                StatusReserva.ATIVA
        );

        Page<Reserva> reservasPage = reservaRepository.findByFuncionarioIdAndStatusIn(funcionarioId, statusAtivos, paginacao);

        return reservasPage.map(ReservaRetornoDTO::new);
    }

    public Page<ReservaRetornoDTO> listarHistoricoReservas(Long funcionarioId, Pageable paginacao) {

        if (!funcionarioRepository.existsById(funcionarioId)) {
            throw new IdNaoEncontradoException("Funcionário não encontrado no sistema.");
        }

        List<StatusReserva> statusInativos = List.of(
                StatusReserva.CONCLUIDA,
                StatusReserva.CANCELADA
        );

        Page<Reserva> historicoPage = reservaRepository.findByFuncionarioIdAndStatusIn(funcionarioId, statusInativos, paginacao);

        return historicoPage.map(ReservaRetornoDTO::new);
    }
}
