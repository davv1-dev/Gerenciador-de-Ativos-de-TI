package br.com.reservasti.controller;

import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.equipamento.dto.*;
import br.com.reservasti.domain.equipamento.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipamentoRetornoDTO> cadastrarEquipamento(@RequestBody @Valid EquipamentoDTO dto, UriComponentsBuilder uriBuilder) {
        EquipamentoRetornoDTO retorno = service.cadastrarEquipamento(dto);
        var uri = uriBuilder.path("/equipamentos/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COMUM','TECNICO')")
    public ResponseEntity<EquipamentoRetornoDTO> detalhar(@PathVariable Long id) {
        EquipamentoRetornoDTO equipamento = service.buscarPorId(id);
        return ResponseEntity.ok(equipamento);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COMUM','TECNICO')")
    public ResponseEntity<Page<EquipamentoRetornoDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {

        Page<EquipamentoRetornoDTO> pagina = service.bucarEquipamento(nome, categoriaId, paginacao);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipamentoRetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EditarEquipamentoDTO dto) {
        EquipamentoRetornoDTO retorno = service.editarEquipamento(id, dto);
        return ResponseEntity.ok(retorno);
    }

    // 👇 AJUSTE 1: Mudamos para @RequestParam. No Angular será: .delete(`/equipamentos/${id}?quantidade=5`)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @RequestParam(required = false) Integer quantidade) { // 👈 required=false para manter a retrocompatibilidade

        service.desativarEquipamento(id, quantidade);
        return ResponseEntity.noContent().build();
    }

    // 👇 AJUSTE 2: Agora usamos um DTO para receber o status e a quantidade no body de forma estruturada.
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,@RequestBody @Valid AlterarStatusDTO dto) {

        service.alterarStatusEquipamento(id, dto.statusEquipamento(), dto.quantidade());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/alocar-departamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> alocarAoDepartamento(@RequestBody @Valid AlocarEquipamentoDTO dto) {
        service.alocarEquipamentoAoDepartamento(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simular-expansao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultadoSimulacaoDTO> simularExpansao(@RequestBody @Valid SimulacaoEquipamentosDTO dto) {
        ResultadoSimulacaoDTO resultado = service.simularExpansao(dto);
        return ResponseEntity.ok(resultado);
    }
}

