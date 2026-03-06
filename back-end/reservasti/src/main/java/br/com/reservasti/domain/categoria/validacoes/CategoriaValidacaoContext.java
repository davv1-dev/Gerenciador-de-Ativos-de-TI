package br.com.reservasti.domain.categoria.validacoes;

import br.com.reservasti.domain.categoria.dto.CategoriaDTO;
//o conceito da context é genialllllllllllllllllll
public record CategoriaValidacaoContext(
        Long id,
        CategoriaDTO dto
) {}