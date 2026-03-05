package br.com.reservasti.domain.categoria;

import br.com.reservasti.domain.categoria.dto.CategoriaAtualizacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    private String descricao;

    public void atualizarInformacoes(CategoriaAtualizacaoDTO dto) {
        if (dto.nome() != null && !dto.nome().isBlank()) {
            this.nome = dto.nome();
        }
        if (dto.descricao() != null) {
            this.descricao = dto.descricao();
        }
    }
}
