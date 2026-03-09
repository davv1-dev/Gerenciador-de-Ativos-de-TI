package br.com.reservasti.domain.funcionario.validacoes;

import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;

public record FuncionarioContext(Long id, FuncionarioDTO dto, FuncionarioAtualizacaoDTO dtoDadosN) {
}
