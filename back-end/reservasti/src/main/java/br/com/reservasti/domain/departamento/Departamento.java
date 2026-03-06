package br.com.reservasti.domain.departamento;

import br.com.reservasti.domain.departamento.dto.DepartamentoAtualizacaoDTO;
import br.com.reservasti.domain.departamento.dto.DepartamentoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String nome;
    @Column(nullable = false,unique = true)
    private String centroDeCusto;

    public Departamento(DepartamentoDTO dto){
        this.nome = dto.nome();
        this.centroDeCusto = dto.centroDeCusto();
    }

    public void atualizarInformacoes(DepartamentoAtualizacaoDTO dto) {
        if(dto.nome()!=null){
            this.nome=dto.nome();
        }

        if(dto.centroDeCusto()!=null){
            this.centroDeCusto= dto.centroDeCusto();
        }
    }
}
