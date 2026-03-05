package br.com.reservasti.domain.departamento.dto;


import br.com.reservasti.domain.departamento.Departamento;

public record DepartamentoRetornoDTO(
        Long id,
        String nome,
        String centroDeCusto
) {
    public DepartamentoRetornoDTO(Departamento departamento) {
        this(
                departamento.getId(),
                departamento.getNome(),
                departamento.getCentroDeCusto()
        );
    }
}
