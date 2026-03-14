package br.com.reservasti.domain.equipamento.dto;

import java.util.List;

public record ResultadoSimulacaoDTO(
        boolean expansaoTotalmenteViavel,
        List<EquipamentoResultadoSimulacaoDTO> detalhes
) {}
