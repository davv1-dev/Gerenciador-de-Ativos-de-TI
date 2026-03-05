package br.com.reservasti.domain.categoria.dto;

import br.com.reservasti.domain.categoria.Categoria;

public record CategoriaRetornoDTO(Long id, String nome, String descricao) {
    public CategoriaRetornoDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
