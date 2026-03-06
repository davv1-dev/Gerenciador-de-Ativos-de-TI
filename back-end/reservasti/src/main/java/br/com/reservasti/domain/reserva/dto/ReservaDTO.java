package br.com.reservasti.domain.reserva.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPrevistaRetirada,
        @NotNull
        @FutureOrPresent
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPrevistaDevolucao
) {
}
