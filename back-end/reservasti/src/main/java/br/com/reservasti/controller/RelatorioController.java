package br.com.reservasti.controller;

import br.com.reservasti.domain.relatorio.RelatorioService;
import br.com.reservasti.domain.relatorio.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/departamentos/{id}")
    public ResponseEntity<RelatorioDepartamentoDTO> gerarResumoPorDepartamento(@PathVariable Long id) {
        RelatorioDepartamentoDTO relatorio = relatorioService.gerarRelatorioPorDepartamento(id);
        return ResponseEntity.ok(relatorio);
    }
    @GetMapping("/geral")
    public ResponseEntity<RelatorioGeralDTO> gerarRelatorioGeral(){
        RelatorioGeralDTO relatorio = relatorioService.gerarRelatorioGeral();
        return ResponseEntity.ok(relatorio);
    }
    @GetMapping("/atrasos")
    public ResponseEntity<List<RelatorioAtrasoDTO>> relatorioAtrasos() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioAtrasos());
    }

    @GetMapping("/falhas")
    public ResponseEntity<List<RelatorioDeFalhaPorMarcaDTO>> relatorioFalhas() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioFalhasPorMarca());
    }

    @GetMapping("/demanda")
    public ResponseEntity<List<PrevisaoDemandaDTO>> previsaoDemanda(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(relatorioService.gerarPrevisaoDemanda(inicio, fim));
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<RelatorioInativosDTO>> relatorioOciosidade(
            @RequestParam(defaultValue = "90") int dias) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioOciosidade(dias));
    }
}