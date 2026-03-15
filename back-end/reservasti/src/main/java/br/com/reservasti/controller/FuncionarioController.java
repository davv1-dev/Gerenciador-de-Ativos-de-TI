package br.com.reservasti.controller;

import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioRetornoDTO;
import br.com.reservasti.domain.funcionario.FuncionarioService;
import br.com.reservasti.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @PostMapping
    public ResponseEntity<FuncionarioRetornoDTO> cadastrar(
            @RequestBody @Valid FuncionarioDTO dto,
            UriComponentsBuilder uriBuilder) {

        FuncionarioRetornoDTO retorno = service.cadastrarFuncionario(dto);
        var uri = uriBuilder.path("/funcionarios/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FuncionarioRetornoDTO>> listar(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao,@RequestParam(required = false) String nome,@RequestParam(required = false) Long departamentoid) {

        return ResponseEntity.ok(service.buscarFuncionario(paginacao,nome,departamentoid));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioRetornoDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COMUM','TECNICO')")

    public ResponseEntity<FuncionarioRetornoDTO> atualizar(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestBody @Valid FuncionarioAtualizacaoDTO dto) {
        Long id = usuarioLogado.getId();
        return ResponseEntity.ok(service.atualizarFuncionario(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.desativarFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pendentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FuncionarioRetornoDTO>> listarPendentes(@PageableDefault(size = 10, sort = {"id"},direction = Sort.Direction.DESC) Pageable paginacao) {
        return ResponseEntity.ok(service.buscarSolicitacoesPendentes(paginacao));
    }

    @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> aprovar(@PathVariable Long id) {
        service.aprovarAcesso(id);
        return ResponseEntity.noContent().build(); // Retorna 204 (Sucesso, sem corpo)
    }

    @PatchMapping("/{id}/negar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> negar(@PathVariable Long id) {
        service.negarAcesso(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/solicitacoes/historico")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FuncionarioRetornoDTO>> listarHistoricoSolicitacoes(
            @PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        return ResponseEntity.ok(service.buscarHistoricoSolicitacoes(paginacao));
    }

}
