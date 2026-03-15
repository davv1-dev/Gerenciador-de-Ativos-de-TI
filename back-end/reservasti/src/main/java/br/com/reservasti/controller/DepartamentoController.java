package br.com.reservasti.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reservasti.domain.departamento.DepartamentoService;
import br.com.reservasti.domain.departamento.dto.DepartamentoAtualizacaoDTO;
import br.com.reservasti.domain.departamento.dto.DepartamentoDTO;
import br.com.reservasti.domain.departamento.dto.DepartamentoRetornoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartamentoRetornoDTO> cadastrar(
            @RequestBody @Valid DepartamentoDTO dto,
            UriComponentsBuilder uriBuilder) {
        DepartamentoRetornoDTO retorno = service.cadastrarDepartamento(dto);
        var uri = uriBuilder.path("/departamentos/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @GetMapping
    public ResponseEntity<Page<DepartamentoRetornoDTO>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return ResponseEntity.ok(service.listarDepartamentos(paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COMUM','TECNICO')")
    public ResponseEntity<DepartamentoRetornoDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdDepartamento(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartamentoRetornoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DepartamentoAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizarDepartamento(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}
