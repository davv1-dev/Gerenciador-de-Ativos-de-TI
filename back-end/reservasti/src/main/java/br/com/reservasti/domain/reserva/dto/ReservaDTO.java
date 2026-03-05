package br.com.reservasti.domain.reserva.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservaDTO(
        @NotNull
        Long funcionarioId,
        @NotNull
        Long equipamentoId,
        @NotNull
        @FutureOrPresent(message = "A data de retirada invalida")
        LocalDate dataPrevistaRetirada,
        @NotNull
        @FutureOrPresent
        LocalDate dataPrevistaDevolucao
) {
}
