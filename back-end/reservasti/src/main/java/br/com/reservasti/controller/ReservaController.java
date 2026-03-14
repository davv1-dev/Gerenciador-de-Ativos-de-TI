package br.com.reservasti.controller;

import br.com.reservasti.domain.reserva.ReservaService;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @PostMapping
    public ResponseEntity<ReservaRetornoDTO> agendar(@RequestBody @Valid ReservaDTO dto, UriComponentsBuilder uriBuilder) {
        ReservaRetornoDTO retorno = service.solicitarReserva(dto);
        var uri = uriBuilder.path("/reservas/{id}").buildAndExpand(retorno.id()).toUri();
        return ResponseEntity.created(uri).body(retorno);
    }

    @PutMapping("/{id}/retirar")
    public ResponseEntity<Void> retirar(@PathVariable Long id,@RequestBody Long idReserva){
        service.retirarEquipamento(id,idReserva);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolver(@PathVariable Long id) {
        service.devolverEquipamento(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build(); 
    }
    @GetMapping("/funcionario/{funcionarioId}/ativas")
    public ResponseEntity<Page<ReservaRetornoDTO>> listarAtivasDoFuncionario(@PathVariable Long funcionarioId, @PageableDefault(size = 10, sort = {"dataPrevistaRetirada"}) Pageable paginacao) {

        Page<ReservaRetornoDTO> pagina = service.listarMinhasReservasAtivas(funcionarioId, paginacao);

        return ResponseEntity.ok(pagina);
    }
    @GetMapping("/funcionario/{funcionarioId}/historico")
    public ResponseEntity<Page<ReservaRetornoDTO>> listarHistoricoDoFuncionario(
            @PathVariable Long funcionarioId,
            @PageableDefault(size = 10, sort = {"dataPrevistaDevolucao"}) Pageable paginacao) {

        Page<ReservaRetornoDTO> pagina = service.listarHistoricoReservas(funcionarioId, paginacao);

        return ResponseEntity.ok(pagina);
    }
}
