package br.com.reservasti.domain.equipamento.dto;

public record EquipamentoResultadoSimulacaoDTO(Long categoriaId,
                                               String nomeCategoria,
                                               Integer quantidadeNecessaria,
                                               Integer quantidadeDisponivel,
                                               Integer quantidadeFaltante,
                                               boolean viavel) {
}
