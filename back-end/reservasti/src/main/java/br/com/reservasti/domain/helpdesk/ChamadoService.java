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
import br.com.reservasti.domain.notificacao.NotificacaoService;
import br.com.reservasti.infra.exceptions.ConcorrenciaException;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private NotificacaoService notificacaoService;


    @Transactional
    public DetalhamentoChamadoDTO abrirChamado(Long id,AberturaChamadoDTO dto) {

        Funcionario solicitante = funcionarioRepository.findById(id)
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

        DetalhamentoChamadoDTO chamadoSalvoDTO = new DetalhamentoChamadoDTO(chamado);

        if (dto.tecnicoId() != null) {
            notificacaoService.notificarTecnico(dto.tecnicoId(), chamadoSalvoDTO);
        }else{
            notificacaoService.notificarTodosTecnicos(chamadoSalvoDTO);
        }

        return chamadoSalvoDTO;
    }

    @Transactional
    public DetalhamentoChamadoDTO resolverChamado(Long chamadoId) {

        Chamado chamadoAtual = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        validadores.forEach(v-> v.validar(new ChamadoContext(chamadoId,null)));

        chamadoAtual.setStatus(StatusChamado.RESOLVIDO);
        chamadoAtual.setDataResolucao(LocalDateTime.now());

        return new DetalhamentoChamadoDTO(chamadoAtual);
    }

    @Transactional(readOnly = true)
    public Page<ResumoChamadoDTO> listarFilaGlobal(Pageable page) {
        return chamadoRepository.findAllByStatusOrderByDataAberturaAsc(StatusChamado.NA_FILA,page)
                .map(ResumoChamadoDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ResumoChamadoDTO> listarFilaPessoal(Long tecnicoId,Pageable page) {
        List<StatusChamado> statusAtivos = List.of(StatusChamado.EM_ATENDIMENTO);

        return chamadoRepository.findAllByTecnicoIdAndStatusInOrderByDataAberturaAsc(tecnicoId, statusAtivos,page)
                .map(ResumoChamadoDTO::new);
    }

    @Transactional
    public DetalhamentoChamadoDTO assumirChamado(Long chamadoId, Long idTecnico) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        Funcionario tecnico = funcionarioRepository.findById(idTecnico)
                .orElseThrow(() -> new IdNaoEncontradoException("Técnico não encontrado"));

        alocarParaTecnico(chamado, tecnico);

        List<Chamado> chamadosRestantes = chamadoRepository.findByStatusOrderByDataAberturaAsc(StatusChamado.NA_FILA);

        for (int i = 0; i < chamadosRestantes.size(); i++) {
            Chamado c = chamadosRestantes.get(i);
            Long novaPosicao = (long) (i + 1);

            notificacaoService.notificarPosicaoFila(c.getSolicitante().getId(), novaPosicao);
        }

        return new DetalhamentoChamadoDTO(chamado);
    }

    @Transactional
    public DetalhamentoChamadoDTO cancelarChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Chamado não encontrado"));

        validadores.forEach(v->v.validar(new ChamadoContext(chamadoId,null)));

        chamado.setStatus(StatusChamado.CANCELADO);
        chamado.setDataResolucao(LocalDateTime.now());

        return new DetalhamentoChamadoDTO(chamado);
    }
    public Page<ResumoChamadoDTO> listarChamadosPorFuncionario(Long solicitanteId, Pageable paginacao) {

        if (!funcionarioRepository.existsById(solicitanteId)) {
            throw new IllegalArgumentException("Funcionário solicitante não encontrado.");
        }

        Page<Chamado> chamadosPage = chamadoRepository.findBySolicitanteId(solicitanteId, paginacao);

        return chamadosPage.map(ResumoChamadoDTO::new);
    }
    public Page<ResumoChamadoDTO> listarHistoricoTecnico(Long tecnicoId, String periodo, Pageable pageable) {
        LocalDateTime agora = LocalDateTime.now();

        LocalDateTime dataLimite = switch (periodo.toLowerCase()) {
            case "7dias" -> agora.minusDays(7);
            case "30dias" -> agora.minusDays(30);
            case "todos" -> LocalDateTime.of(2000, 1, 1, 0, 0);
            default -> agora.with(java.time.LocalTime.MIN);
        };

        return chamadoRepository.findByTecnico_IdAndStatusAndDataResolucaoAfter(tecnicoId,StatusChamado.RESOLVIDO,dataLimite,pageable)
                .map(ResumoChamadoDTO::new);
    }

    private void alocarParaTecnico(Chamado chamado, Funcionario tecnico) {

        chamado.setTecnico(tecnico);

        boolean isTecnicoOcupado = chamadoRepository.existsByTecnicoIdAndStatus(tecnico.getId(),StatusChamado.EM_ATENDIMENTO);

        if (isTecnicoOcupado) {
            throw new ConcorrenciaException("Tecnico ja possui um Chamado em Atendimento");
        }else{
            chamado.setStatus(StatusChamado.EM_ATENDIMENTO);
        }
    }
}