package br.com.reservasti.controller;

import br.com.reservasti.domain.reserva.ReservaService;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import br.com.reservasti.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<ReservaRetornoDTO> agendar(@RequestBody @Valid ReservaDTO dto, UriComponentsBuilder uriBuilder) {
        ReservaRetornoDTO retorno = service.solicitarReserva(dto);
        var uri = uriBuilder.path("/reservas/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @PutMapping("/retirar")
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<Void> retirar(@AuthenticationPrincipal Usuario usuarioLogado,@RequestBody Long idReserva){
        service.retirarEquipamento(usuarioLogado.getId(), idReserva);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/devolver")
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<Void> devolver(@RequestBody Long id) {
        service.devolverEquipamento(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/cancelar")
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<Void> cancelarReserva(@RequestBody Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build(); 
    }
    @GetMapping("/minhas/ativas")
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<Page<ReservaRetornoDTO>> listarAtivasDoFuncionario(@AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(size = 10, sort = {"dataPrevistaRetirada"}) Pageable paginacao) {
        Long funcionarioId = usuarioLogado.getId();

        Page<ReservaRetornoDTO> pagina = service.listarMinhasReservasAtivas(funcionarioId, paginacao);

        return ResponseEntity.ok(pagina);
    }
    @GetMapping("/minhas/historico")
    @PreAuthorize("hasAnyRole('COMUM','TECNICO','ADMIN')")
    public ResponseEntity<Page<ReservaRetornoDTO>> listarHistoricoDoFuncionario(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @PageableDefault(size = 10, sort = {"dataPrevistaDevolucao"}) Pageable paginacao) {
        Long funcionarioId = usuarioLogado.getId();
        Page<ReservaRetornoDTO> pagina = service.listarHistoricoReservas(funcionarioId, paginacao);

        return ResponseEntity.ok(pagina);
    }
    @GetMapping("/historico/funcionarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservaRetornoDTO>> listarHistoricoDoFuncionario(
            @PathVariable("id") Long funcionarioId,
            @PageableDefault(size = 10, sort = {"dataPrevistaDevolucao"}) Pageable paginacao) {
        Page<ReservaRetornoDTO> pagina = service.listarHistoricoReservas(funcionarioId, paginacao);

        return ResponseEntity.ok(pagina);
    }
}
