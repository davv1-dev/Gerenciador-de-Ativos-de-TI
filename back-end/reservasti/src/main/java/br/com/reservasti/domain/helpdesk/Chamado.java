package br.com.reservasti.domain.helpdesk;


import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.helpdesk.dto.AberturaChamadoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long versao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id")
    private Funcionario solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Funcionario tecnico;

    @Enumerated(EnumType.STRING)
    private TipoProblema tipoProblema;

    @Column(columnDefinition = "TEXT")
    private String descricaoDetalhada;

    @Column(name = "localizacao_exata")
    private String localizacao;

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataResolucao;

    public Chamado(AberturaChamadoDTO dto,Funcionario solicitante){
        this.solicitante=solicitante;
        this.tipoProblema=dto.tipoProblema();
        this.descricaoDetalhada= dto.descricaoDetalhada();
        this.localizacao=dto.localizacao();
        this.dataAbertura=LocalDateTime.now();
    }
}
