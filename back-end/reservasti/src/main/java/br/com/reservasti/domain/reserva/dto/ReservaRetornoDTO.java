package br.com.reservasti.domain.reserva.dto;
import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.StatusReserva;

import java.time.LocalDate;

public record ReservaRetornoDTO(
        Long id,
        String nomeFuncionario,
        String nomeEquipamento,
        LocalDate dataPrevistaRetirada,
        LocalDate dataPrevistaDevolucao,
        StatusReserva status
) {
    public ReservaRetornoDTO(Reserva r) {
        this(
                r.getId(),
                r.getFuncionario().getNomeCompleto(),
                r.getEquipamento().getNome(),
                r.getDataPrevistaRetirada(),
                r.getDataPrevistaDevolucao(),
                r.getStatus()
        );
    }
}