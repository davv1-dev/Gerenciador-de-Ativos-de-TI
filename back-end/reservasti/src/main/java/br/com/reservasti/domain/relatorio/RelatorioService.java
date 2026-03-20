package br.com.reservasti.domain.relatorio;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.departamento.DepartamentoRepository;
import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.equipamento.dto.EquipamentoRetornoDTO;
import br.com.reservasti.domain.relatorio.dto.*;
import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.infra.exceptions.IdNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public RelatorioGeralDTO gerarRelatorioGeral() {
        long total = equipamentoRepository.count();
        long disponiveis = equipamentoRepository.countByStatus(StatusEquipamento.DISPONIVEL);
        long emUso = equipamentoRepository.countByStatus(StatusEquipamento.EM_USO);
        long manutencao = equipamentoRepository.countByStatus(StatusEquipamento.MANUTENCAO);

        return new RelatorioGeralDTO(total, disponiveis, emUso, manutencao);
    }

    @Transactional(readOnly = true)
    public RelatorioDepartamentoDTO gerarRelatorioPorDepartamento(Long departamentoId) {
        Departamento depto = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new IdNaoEncontradoException("Departamento não encontrado"));

        long total = equipamentoRepository.countByDepartamentoId(departamentoId);
        long disponiveis = equipamentoRepository.countByDepartamentoIdAndStatus(departamentoId, StatusEquipamento.DISPONIVEL);
        long emUso = equipamentoRepository.countByDepartamentoIdAndStatus(departamentoId, StatusEquipamento.EM_USO);
        long manutencao = equipamentoRepository.countByDepartamentoIdAndStatus(departamentoId, StatusEquipamento.MANUTENCAO);

        return new RelatorioDepartamentoDTO(depto.getNome(), total, disponiveis, emUso, manutencao);
    }
    @Transactional(readOnly = true)
    public List<RelatorioAtrasoDTO> gerarRelatorioAtrasos() {
        LocalDate hoje = LocalDate.now();
        List<StatusReserva> statusAtivos = List.of(StatusReserva.ATIVA);

        List<Reserva> atrasadas = reservaRepository.findReservasAtrasadas(statusAtivos, hoje);

        return atrasadas.stream().map(r -> {
            long dias = ChronoUnit.DAYS.between(r.getDataPrevistaDevolucao(), hoje);
            String departamento = r.getEquipamento().getDepartamento() != null ?
                    r.getEquipamento().getDepartamento().getNome() : "Sem Registo";

            return new RelatorioAtrasoDTO(r.getFuncionario().getNomeCompleto(), departamento,r.getEquipamento().getNumeroPatrimonio(),dias);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RelatorioDeFalhaPorMarcaDTO> gerarRelatorioFalhasPorMarca() {
        return equipamentoRepository.findFalhasPorMarca(StatusEquipamento.MANUTENCAO);
    }

    @Transactional(readOnly = true)
    public List<PrevisaoDemandaDTO> gerarPrevisaoDemanda(LocalDate inicio, LocalDate fim) {
        return reservaRepository.findDemandaPorPeriodo(inicio, fim);
    }
    @Transactional(readOnly = true)
    public List<RelatorioInativosDTO> gerarRelatorioOciosidade(int diasLimite) {
        LocalDateTime dataCorte = LocalDateTime.now().minusDays(diasLimite);
        List<Equipamento> ociosos = equipamentoRepository.findEquipamentosOciosos(dataCorte);

        return ociosos.stream().map(e -> {
            String departamento = e.getDepartamento() != null ? e.getDepartamento().getNome() : "Stock Central TI";
            return new RelatorioInativosDTO(e.getNumeroPatrimonio(),e.getNome(), departamento,dataCorte
            );
        }).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Page<EquipamentoRetornoDTO> relatorioGarantiasProximasDoVencimento(Pageable paginacao) {
        LocalDate dataMinima = LocalDate.of(2000, 1, 1);
        LocalDate daquiA30Dias = LocalDate.now().plusDays(30);

        return equipamentoRepository.findAllByDataFimGarantiaBetween(dataMinima, daquiA30Dias, paginacao)
                .map(EquipamentoRetornoDTO::new);
    }
}
