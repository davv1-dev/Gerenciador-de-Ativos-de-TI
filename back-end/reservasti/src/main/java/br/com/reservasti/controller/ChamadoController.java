package br.com.reservasti.controller;

import br.com.reservasti.domain.helpdesk.ChamadoService;
import br.com.reservasti.domain.helpdesk.dto.AberturaChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.DetalhamentoChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.ResumoChamadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private ChamadoService service;

    @PostMapping
    public ResponseEntity<DetalhamentoChamadoDTO> abrirChamado(@RequestBody @Valid AberturaChamadoDTO dto,UriComponentsBuilder uriBuilder) {

        DetalhamentoChamadoDTO chamadoCriado = service.abrirChamado(dto);

        URI endereco = uriBuilder.path("/chamados/{id}").buildAndExpand(chamadoCriado.id()).toUri();

        return ResponseEntity.created(endereco).body(chamadoCriado);
    }
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<DetalhamentoChamadoDTO> resolverChamado(@PathVariable Long id) {
        DetalhamentoChamadoDTO chamadoResolvido = service.resolverChamado(id);

        return ResponseEntity.ok(chamadoResolvido);
    }
    @GetMapping("/fila-global")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarFilaGlobal(@PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(service.listarFilaGlobal(pageable));
    }
    @GetMapping("/fila-pessoal/{tecnicoId}")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarFilaPessoal(@PathVariable Long tecnicoId,@PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarFilaPessoal(tecnicoId,pageable));
    }
    //Lembrar de mudar esse e o metodo de cima quando eu adicionar o springsecurity
    @PatchMapping("/{idChamado}/assumir")
    public ResponseEntity<DetalhamentoChamadoDTO> assumirChamado(@PathVariable Long idChamado,@RequestBody @Valid Long idTecnico) {

        DetalhamentoChamadoDTO chamadoAssumido = service.assumirChamado(idChamado, idTecnico);
        return ResponseEntity.ok(chamadoAssumido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalhamentoChamadoDTO> cancelarChamado(@PathVariable Long id) {

        DetalhamentoChamadoDTO chamadoCancelado = service.cancelarChamado(id);
        return ResponseEntity.ok(chamadoCancelado);
    }
    //mudar esse aqui tbm quando adicionar o spring security
    @GetMapping("/solicitante/{solicitanteId}")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarMeusChamados(@PathVariable Long solicitanteId,@PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable paginacao) {

        Page<ResumoChamadoDTO> pagina = service.listarChamadosPorFuncionario(solicitanteId, paginacao);

        return ResponseEntity.ok(pagina);
    }
    @GetMapping("/historico/{tecnicoId}")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarHistoricoTecnico(@PathVariable Long tecnicoId,@RequestParam(defaultValue = "hoje") String periodo,@PageableDefault(size = 5, sort = "dataAbertura", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResumoChamadoDTO> historico = service.listarHistoricoTecnico(tecnicoId, periodo, pageable);

        return ResponseEntity.ok(historico);
    }
}
