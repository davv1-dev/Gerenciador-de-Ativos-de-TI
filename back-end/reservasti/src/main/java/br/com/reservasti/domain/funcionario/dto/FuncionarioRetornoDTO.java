package br.com.reservasti.domain.funcionario.dto;

import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.StatusAcesso;

import java.time.LocalDateTime;

public record FuncionarioRetornoDTO(
        Long id,
        String nomeCompleto,
        String email,
        String cpf,
        String numeroDeTelefone,
        String nomeDepartamento,
        StatusAcesso statusAcesso,
        LocalDateTime dataSolicitacao,
        Boolean ativo
) {
    public FuncionarioRetornoDTO(Funcionario funcionario) {
        this(
                funcionario.getId(),
                funcionario.getNomeCompleto(),
                funcionario.getEmail(),
                funcionario.getCpf(),
                funcionario.getNumeroDeTelefone(),
                funcionario.getDepartamento().getNome(),// Pega o nome do departamento pelo relacionamento
                funcionario.getStatusAcesso(),
                LocalDateTime.now(),
                funcionario.getAtivo()
        );
    }
}
