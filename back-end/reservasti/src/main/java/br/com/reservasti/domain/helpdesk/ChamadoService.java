package br.com.reservasti.domain.helpdesk;

import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.helpdesk.dto.AberturaChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.DetalhamentoChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.ResumoChamadoDTO;
import br.com.reservasti.domain.helpdesk.validacoes.ChamadoContext;
import br.com.reservasti.domain.helpdesk.validacoes.IValidatorChamado;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import br.com.reservasti.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private List<IValidatorChamado> validadores;

    @Transactional
    public DetalhamentoChamadoDTO abrirChamado(AberturaChamadoDTO dto) {

        Funcionario solicitante = funcionarioRepository.findById(dto.solicitanteId())
                .orElseThrow(() -> new IdNaoEncontradoException("Solicitante não encontrado"));

        Chamado chamado = new Chamado(dto, solicitante);

        if (dto.equipamentoId() != null) {
            Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Equipamento não encontrado"));
            chamado.setEquipamento(equipamento);
        }

        if (dto.tecnicoId() != null) {
            Funcionario tecnico = funcionarioRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Técnico não encontrado"));

            alocarParaTecnico(chamado, tecnico);
        } else {
            chamado.setStatus(StatusChamado.NA_FILA);
        }

        chamadoRepository.save(chamado);

        return new DetalhamentoChamadoDTO(chamado);
    }

    @Transactional
    public DetalhamentoChamadoDTO resolverChamado(Long chamadoId) {

        Chamado chamadoAtual = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        validadores.forEach(v-> v.validar(new ChamadoContext(chamadoId,null)));

        chamadoAtual.setStatus(StatusChamado.RESOLVIDO);
        chamadoAtual.setDataResolucao(LocalDateTime.now());

        puxarProximoDaFilaParaTecnico(chamadoAtual.getTecnico());

        return new DetalhamentoChamadoDTO(chamadoAtual);
    }

    @Transactional(readOnly = true)
    public List<ResumoChamadoDTO> listarFilaGlobal() {
        return chamadoRepository.findAllByStatusOrderByDataAberturaAsc(StatusChamado.NA_FILA)
                .stream()
                .map(ResumoChamadoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResumoChamadoDTO> listarFilaPessoal(Long tecnicoId) {
        List<StatusChamado> statusAtivos = List.of(StatusChamado.EM_ATENDIMENTO, StatusChamado.FILA_DO_TECNICO);

        return chamadoRepository.findAllByTecnicoIdAndStatusInOrderByDataAberturaAsc(tecnicoId, statusAtivos)
                .stream()
                .map(ResumoChamadoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public DetalhamentoChamadoDTO assumirChamado(Long chamadoId, Long idTecnico) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        validadores.forEach(v->v.validar(new ChamadoContext(chamadoId,null)));

        Funcionario tecnico = funcionarioRepository.findById(idTecnico)
                .orElseThrow(() -> new IdNaoEncontradoException("Técnico não encontrado"));

        alocarParaTecnico(chamado, tecnico);

        return new DetalhamentoChamadoDTO(chamado);
    }

    @Transactional
    public DetalhamentoChamadoDTO cancelarChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        validadores.forEach(v->v.validar(new ChamadoContext(chamadoId,null)));

        StatusChamado statusAnterior = chamado.getStatus();
        Funcionario tecnicoAlocado = chamado.getTecnico();

        chamado.setStatus(StatusChamado.CANCELADO);
        chamado.setDataResolucao(LocalDateTime.now());

        if (statusAnterior == StatusChamado.EM_ATENDIMENTO && tecnicoAlocado != null) {
            puxarProximoDaFilaParaTecnico(tecnicoAlocado);
        }

        return new DetalhamentoChamadoDTO(chamado);
    }


    private void puxarProximoDaFilaParaTecnico(Funcionario tecnico) {
        Optional<Chamado> proximoDaFilaPessoal = chamadoRepository
                .findFirstByTecnicoIdAndStatusOrderByDataAberturaAsc(tecnico.getId(), StatusChamado.FILA_DO_TECNICO);

        if (proximoDaFilaPessoal.isPresent()) {
            Chamado proximo = proximoDaFilaPessoal.get();
            proximo.setStatus(StatusChamado.EM_ATENDIMENTO);
            return;
        }

        Optional<Chamado> proximoDaFilaGlobal = chamadoRepository
                .findFirstByStatusOrderByDataAberturaAsc(StatusChamado.NA_FILA);

        if (proximoDaFilaGlobal.isPresent()) {
            Chamado proximo = proximoDaFilaGlobal.get();
            proximo.setTecnico(tecnico);
            proximo.setStatus(StatusChamado.EM_ATENDIMENTO);
        }
    }
    private void alocarParaTecnico(Chamado chamado, Funcionario tecnico) {
        chamado.setTecnico(tecnico);

        boolean isTecnicoOcupado = chamadoRepository.existsByTecnicoIdAndStatus(
                tecnico.getId(),
                StatusChamado.EM_ATENDIMENTO
        );

        if (isTecnicoOcupado) {
            chamado.setStatus(StatusChamado.FILA_DO_TECNICO);
        } else {
            chamado.setStatus(StatusChamado.EM_ATENDIMENTO);
        }
    }
}