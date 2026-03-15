package br.com.reservasti.domain.helpdesk.dto;

import br.com.reservasti.domain.helpdesk.TipoProblema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AberturaChamadoDTO(
        Long equipamentoId,

        @NotNull(message = "Selecione o tipo de problema")
        TipoProblema tipoProblema,

        @NotBlank(message = "Descreva o problema")
        String descricaoDetalhada,

        @NotBlank(message = "Informe onde você ou o equipamento estão")
        String localizacao,

        Long tecnicoId
) {}
