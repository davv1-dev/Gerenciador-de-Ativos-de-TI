package br.com.reservasti.domain.equipamento;

import br.com.reservasti.domain.categoria.Categoria;
import br.com.reservasti.domain.equipamento.dto.EditarEquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "equipamentos")
@Getter
@Setter
public class Equipamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private String marca;
    private String modelo;

    @Column(unique = true,nullable = false)
    private String numeroPatrimonio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamento status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categoria categoria;

    public Equipamento(EquipamentoDTO dto, Categoria categoria) {
        this.nome = dto.nome();
        this.marca = dto.marca();
        this.modelo = dto.modelo();
        this.numeroPatrimonio = dto.numeroPatrimonio();
        this.status = StatusEquipamento.DISPONIVEL;
        this.categoria = categoria;
    }
    public void atualizarInformacoes(EditarEquipamentoDTO dados,Categoria categorianova) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.marca() != null) {
            this.marca = dados.marca();
        }
        if (dados.modelo() != null) {
            this.modelo = dados.modelo();
        }
        if (categorianova != null) {
            this.categoria = categorianova;
        }

    }
}

