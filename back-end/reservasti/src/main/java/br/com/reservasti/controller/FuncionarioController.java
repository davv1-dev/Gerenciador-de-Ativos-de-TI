package br.com.reservasti.controller;

import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioRetornoDTO;
import br.com.reservasti.domain.funcionario.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<FuncionarioRetornoDTO>> listar(
            @PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {

        // Podemos adicionar o Specifications aqui depois para filtrar por nome/departamento!
        return ResponseEntity.ok(service.listarAtivos(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioRetornoDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioRetornoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioAtualizacaoDTO dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
