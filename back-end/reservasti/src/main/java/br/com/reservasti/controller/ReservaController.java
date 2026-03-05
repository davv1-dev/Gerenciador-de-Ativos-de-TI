package br.com.reservasti.controller;

import br.com.reservasti.domain.reserva.ReservaService;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolver(@PathVariable Long id) {
        service.devolverEquipamento(id);
        return ResponseEntity.noContent().build();
    }
}
