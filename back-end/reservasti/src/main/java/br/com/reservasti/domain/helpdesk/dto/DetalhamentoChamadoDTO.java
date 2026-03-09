package br.com.reservasti.domain.helpdesk.dto;

import br.com.reservasti.domain.helpdesk.Chamado;

import java.time.LocalDateTime;

public record DetalhamentoChamadoDTO(
        Long id,
        String nomeSolicitante,
        String nomeTecnico,
        String tipoProblema,
        String status,
        LocalDateTime dataAbertura
) {
    public DetalhamentoChamadoDTO(Chamado chamado) {
        this(
                chamado.getId(),
                chamado.getSolicitante().getNomeCompleto(),
                chamado.getTecnico() !=null ? chamado.getTecnico().getNomeCompleto() : null,
                chamado.getTipoProblema().name(),
                chamado.getStatus().name(),
                chamado.getDataAbertura()
        );
    }
}
