package br.com.reservasti.domain.funcionario.dto;



public record FuncionarioAtualizacaoDTO(
        String nomeCompleto,
        String email,
        String numeroDeTelefone,
        Long departamentoId
) {
}