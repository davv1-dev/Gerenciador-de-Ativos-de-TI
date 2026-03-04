package br.com.reservasti.domain.funcionario.dto;

import br.com.reservasti.domain.funcionario.Funcionario;

public record FuncionarioRetornoDTO(
        Long id,
        String nomeCompleto,
        String email,
        String cpf,
        String numeroDeTelefone,
        String nomeDepartamento, // Dica Sênior: Retorne o nome, não o ID! Facilita a vida do Front-end.
        Boolean ativo
) {
    // Construtor mágico que converte a Entidade para este DTO
    public FuncionarioRetornoDTO(Funcionario funcionario) {
        this(
                funcionario.getId(),
                funcionario.getNomeCompleto(),
                funcionario.getEmail(),
                funcionario.getCpf(),
                funcionario.getNumeroDeTelefone(),
                funcionario.getDepartamento().getNome(), // Pega o nome do departamento pelo relacionamento
                funcionario.getAtivo()
        );
    }
}
