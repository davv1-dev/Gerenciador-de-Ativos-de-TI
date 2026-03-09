package br.com.reservasti.controller;

import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.equipamento.dto.AlocarEquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EditarEquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoRetornoDTO;
import br.com.reservasti.domain.equipamento.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService service;

    @PostMapping
    public ResponseEntity<EquipamentoRetornoDTO> cadastrarEquipamento(@RequestBody @Valid EquipamentoDTO dto, UriComponentsBuilder uriBuilder) {

        EquipamentoRetornoDTO retorno = service.cadastrarEquipamento(dto);

        var uri = uriBuilder.path("/equipamentos/{id}").buildAndExpand(retorno.id()).toUri();

        return ResponseEntity.created(uri).body(retorno);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoRetornoDTO> detalhar(@PathVariable Long id) {
        EquipamentoRetornoDTO equipamento = service.buscarPorId(id);
        return ResponseEntity.ok(equipamento);
    }

    @GetMapping
    public ResponseEntity<Page<EquipamentoRetornoDTO>> listar(@RequestParam(required = false) String nome, @RequestParam(required = false) Long categoriaId, @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {

        Page<EquipamentoRetornoDTO> pagina = service.bucarEquipamento(nome, categoriaId, paginacao);

        return ResponseEntity.ok(pagina);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoRetornoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EditarEquipamentoDTO dto) {

        EquipamentoRetornoDTO retorno = service.editarEquipamento(id, dto);

        return ResponseEntity.ok(retorno);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        service.desativarEquipamento(id);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,@RequestBody StatusEquipamento novoStatus) {

        service.alterarStatusEquipamento(id, novoStatus);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/alocar-departamento")
    public ResponseEntity<Void> alocarAoDepartamento(
            @RequestBody @Valid AlocarEquipamentoDTO dto) {

        service.alocarEquipamentoAoDepartamento(dto);

        return ResponseEntity.noContent().build();
    }
}

