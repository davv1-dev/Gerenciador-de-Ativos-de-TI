package br.com.reservasti.controller;

import br.com.reservasti.domain.helpdesk.ChamadoService;
import br.com.reservasti.domain.helpdesk.dto.AberturaChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.DetalhamentoChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.ResumoChamadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<DetalhamentoChamadoDTO> abrirChamado(@RequestBody @Valid AberturaChamadoDTO dto,UriComponentsBuilder uriBuilder) {

        DetalhamentoChamadoDTO chamadoCriado = chamadoService.abrirChamado(dto);

        URI endereco = uriBuilder.path("/chamados/{id}").buildAndExpand(chamadoCriado.id()).toUri();

        return ResponseEntity.created(endereco).body(chamadoCriado);
    }
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<DetalhamentoChamadoDTO> resolverChamado(@PathVariable Long id) {
        DetalhamentoChamadoDTO chamadoResolvido = chamadoService.resolverChamado(id);

        return ResponseEntity.ok(chamadoResolvido);
    }
    @GetMapping("/fila-global")
    public ResponseEntity<List<ResumoChamadoDTO>> listarFilaGlobal() {
        return ResponseEntity.ok(chamadoService.listarFilaGlobal());
    }
    @GetMapping("/fila-pessoal/{tecnicoId}")
    public ResponseEntity<List<ResumoChamadoDTO>> listarFilaPessoal(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(chamadoService.listarFilaPessoal(tecnicoId));
    }
    //Lembrar de mudar esse e o metodo de cima quando eu adicionar o springsecurity
    @PatchMapping("/{idChamado}/assumir")
    public ResponseEntity<DetalhamentoChamadoDTO> assumirChamado(@PathVariable Long idChamado,@RequestBody @Valid Long idTecnico) {

        DetalhamentoChamadoDTO chamadoAssumido = chamadoService.assumirChamado(idChamado, idTecnico);
        return ResponseEntity.ok(chamadoAssumido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalhamentoChamadoDTO> cancelarChamado(@PathVariable Long id) {

        DetalhamentoChamadoDTO chamadoCancelado = chamadoService.cancelarChamado(id);
        return ResponseEntity.ok(chamadoCancelado);
    }
}
