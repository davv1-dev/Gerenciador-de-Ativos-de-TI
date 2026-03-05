package br.com.reservasti.controller;

import br.com.reservasti.domain.categoria.CategoriaService;
import br.com.reservasti.domain.categoria.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaRetornoDTO> cadastrar(@RequestBody @Valid CategoriaDTO dto, UriComponentsBuilder uriBuilder) {
        CategoriaRetornoDTO retorno = service.cadastrar(dto);
        var uri = uriBuilder.path("/categorias/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaRetornoDTO>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaRetornoDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaRetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}