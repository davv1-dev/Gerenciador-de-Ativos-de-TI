package br.com.reservasti.controller;

import br.com.reservasti.domain.helpdesk.ChamadoService;
import br.com.reservasti.domain.helpdesk.dto.AberturaChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.DetalhamentoChamadoDTO;
import br.com.reservasti.domain.helpdesk.dto.ResumoChamadoDTO;
import br.com.reservasti.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAnyRole('COMUM','ADMIN')")
    public ResponseEntity<DetalhamentoChamadoDTO> abrirChamado(@AuthenticationPrincipal Usuario usuarioLogado,@RequestBody @Valid AberturaChamadoDTO dto,UriComponentsBuilder uriBuilder) {

        DetalhamentoChamadoDTO chamadoCriado = service.abrirChamado(usuarioLogado.getId(),dto);

        URI endereco = uriBuilder.path("/chamados/{id}").buildAndExpand(chamadoCriado.id()).toUri();

        return ResponseEntity.created(endereco).body(chamadoCriado);
    }
    @PatchMapping("/{id}/resolver")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<DetalhamentoChamadoDTO> resolverChamado(@PathVariable Long id) {
        DetalhamentoChamadoDTO chamadoResolvido = service.resolverChamado(id);

        return ResponseEntity.ok(chamadoResolvido);
    }
    @GetMapping("/fila-global")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarFilaGlobal(@PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(service.listarFilaGlobal(pageable));
    }
    @GetMapping("/fila-pessoal")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarFilaPessoal(@AuthenticationPrincipal Usuario usuarioLogado,@PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.ASC) Pageable pageable) {
        Long tecnicoId = usuarioLogado.getId();
        return ResponseEntity.ok(service.listarFilaPessoal(tecnicoId,pageable));
    }
    @PatchMapping("/{idChamado}/assumir")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<DetalhamentoChamadoDTO> assumirChamado(
            @PathVariable Long idChamado,
            @AuthenticationPrincipal Usuario tecnicoLogado) {

        DetalhamentoChamadoDTO chamadoAssumido = service.assumirChamado(idChamado, tecnicoLogado.getId());
        return ResponseEntity.ok(chamadoAssumido);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMUM', 'TECNICO', 'ADMIN')")
    public ResponseEntity<DetalhamentoChamadoDTO> cancelarChamado(@PathVariable Long id) {

        DetalhamentoChamadoDTO chamadoCancelado = service.cancelarChamado(id);
        return ResponseEntity.ok(chamadoCancelado);
    }
    @GetMapping("/meus-chamados")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarMeusChamados(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable paginacao) {

        Page<ResumoChamadoDTO> pagina = service.listarChamadosPorFuncionario(usuarioLogado.getId(), paginacao);
        return ResponseEntity.ok(pagina);
    }
    @GetMapping("/historico")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Page<ResumoChamadoDTO>> listarHistoricoTecnico( @AuthenticationPrincipal Usuario usuarioLogado,@RequestParam(defaultValue = "hoje") String periodo,@PageableDefault(size = 5, sort = "dataAbertura", direction = Sort.Direction.DESC) Pageable pageable) {
        Long tecnicoId = usuarioLogado.getId();
        Page<ResumoChamadoDTO> historico = service.listarHistoricoTecnico(tecnicoId, periodo, pageable);

        return ResponseEntity.ok(historico);
    }
}
