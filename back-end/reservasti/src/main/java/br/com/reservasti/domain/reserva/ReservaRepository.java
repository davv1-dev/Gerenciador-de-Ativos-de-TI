package br.com.reservasti.domain.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    @Query("""
            SELECT COUNT(r) > 0 FROM Reserva r 
            WHERE r.equipamento.id = :equipamentoId 
            AND r.status IN ('AGENDADA', 'ATIVA') 
            AND r.dataPrevistaRetirada <= :dataDevolucao 
            AND r.dataPrevistaDevolucao >= :dataRetirada
            """)
    boolean existeConflitoDeHorario(@Param("equipamentoId") Long equipamentoId, @Param("dataRetirada") LocalDate dataRetirada,
            @Param("dataDevolucao") LocalDate dataDevolucao
    );
}