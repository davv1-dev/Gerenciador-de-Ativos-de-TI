package br.com.reservasti.domain.helpdesk.dto;

import br.com.reservasti.domain.helpdesk.Chamado;

import java.time.LocalDateTime;

public record ResumoChamadoDTO(
        Long id,
        String nomeSolicitante,
        String tipoProblema,
        String localizacao,
        String descricao,
        String status,
        LocalDateTime dataAbertura
) {
    public ResumoChamadoDTO(Chamado chamado) {
        this(
                chamado.getId(),chamado.getSolicitante().getNomeCompleto(),chamado.getTipoProblema().name(),chamado.getLocalizacao(), chamado.getDescricaoDetalhada(), chamado.getStatus().name(),chamado.getDataAbertura()
        );
    }
}