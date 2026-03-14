package br.com.reservasti.domain.reserva.validacoes;

import br.com.reservasti.domain.reserva.dto.ReservaDTO;

public record ReservaContext(Long idReserva, ReservaDTO dto) {
}
