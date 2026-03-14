package br.com.reservasti.domain.reserva;

import br.com.reservasti.domain.relatorio.dto.PrevisaoDemandaDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByFuncionarioIdAndStatus(Long idFuncionario,StatusReserva status);
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

    long countByFuncionarioIdAndStatusIn(@NotNull Long aLong, List<StatusReserva> statusAtivos);
    @Query("SELECT r FROM Reserva r WHERE r.status IN :status AND r.dataPrevistaDevolucao < :hoje")
    List<Reserva> findReservasAtrasadas(
            @Param("status") List<StatusReserva> status,
            @Param("hoje") LocalDate hoje
    );

    @Query("SELECT new br.com.reservasti.domain.relatorio.dto.PrevisaoDemandaDTO(e.categoria.nome, COUNT(r)) " +
            "FROM Reserva r JOIN r.equipamento e " +
            "WHERE r.status IN ('AGENDADA', 'ATIVA', 'EM_USO') " +
            "AND r.dataPrevistaRetirada <= :fim AND r.dataPrevistaDevolucao >= :inicio " +
            "GROUP BY e.categoria.nome")
    List<PrevisaoDemandaDTO> findDemandaPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
    Page<Reserva> findByFuncionarioIdAndStatusIn(Long funcionarioId, List<StatusReserva> status, Pageable paginacao);
}