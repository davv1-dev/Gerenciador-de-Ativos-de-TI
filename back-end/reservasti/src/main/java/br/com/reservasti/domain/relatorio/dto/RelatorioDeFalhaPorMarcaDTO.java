package br.com.reservasti.domain.relatorio.dto;

public record RelatorioDeFalhaPorMarcaDTO(String marca,
                                          String modelo,
                                          long totalEmManutencao) {
}
