package br.com.reservasti.domain.reserva;

import jakarta.persistence.Entity;
import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.funcionario.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    private LocalDateTime dataAgendamento;
    private LocalDate dataPrevistaRetirada;
    private LocalDate dataPrevistaDevolucao;
    private LocalDateTime dataDevolucaoReal;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    public Reserva(Funcionario funcionario, Equipamento equipamento, LocalDate retirada, LocalDate devolucao) {
        this.funcionario = funcionario;
        this.equipamento = equipamento;
        this.dataPrevistaRetirada = retirada;
        this.dataPrevistaDevolucao = devolucao;
        this.status = StatusReserva.AGENDADA;
    }

    @PrePersist
    public void preencherDataAgendamento() {
        this.dataAgendamento = LocalDateTime.now();
    }

    public void iniciarReserva() {
        this.status = StatusReserva.ATIVA;
    }

    public void finalizarReserva() {
        this.status = StatusReserva.CONCLUIDA;
        this.dataDevolucaoReal = LocalDateTime.now();
    }

    public void cancelarReserva() {
        this.status = StatusReserva.CANCELADA;
    }
}
