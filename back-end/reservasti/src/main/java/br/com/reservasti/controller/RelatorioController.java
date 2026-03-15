package br.com.reservasti.controller;

import br.com.reservasti.domain.equipamento.dto.EquipamentoRetornoDTO;
import br.com.reservasti.domain.relatorio.RelatorioService;
import br.com.reservasti.domain.relatorio.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/departamentos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RelatorioDepartamentoDTO> gerarResumoPorDepartamento(@PathVariable Long id) {
        RelatorioDepartamentoDTO relatorio = relatorioService.gerarRelatorioPorDepartamento(id);
        return ResponseEntity.ok(relatorio);
    }
    @GetMapping("/geral")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RelatorioGeralDTO> gerarRelatorioGeral(){
        RelatorioGeralDTO relatorio = relatorioService.gerarRelatorioGeral();
        return ResponseEntity.ok(relatorio);
    }
    @GetMapping("/atrasos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RelatorioAtrasoDTO>> relatorioAtrasos() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioAtrasos());
    }

    @GetMapping("/falhas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RelatorioDeFalhaPorMarcaDTO>> relatorioFalhas() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioFalhasPorMarca());
    }

    @GetMapping("/demanda")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrevisaoDemandaDTO>> previsaoDemanda(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(relatorioService.gerarPrevisaoDemanda(inicio, fim));
    }

    @GetMapping("/inativos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RelatorioInativosDTO>> relatorioOciosidade(
            @RequestParam(defaultValue = "90") int dias) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioOciosidade(dias));
    }
    @GetMapping("/garantias-vencendo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EquipamentoRetornoDTO>> relatorioGarantiasVencendo(
            @PageableDefault(size = 10, sort = "dataFimGarantia", direction = Sort.Direction.ASC) Pageable paginacao) {

        var relatorioPaginado = relatorioService.relatorioGarantiasProximasDoVencimento(paginacao);
        return ResponseEntity.ok(relatorioPaginado);
    }
}