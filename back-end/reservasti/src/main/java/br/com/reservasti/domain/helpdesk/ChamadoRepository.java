package br.com.reservasti.domain.helpdesk;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChamadoRepository extends JpaRepository<Chamado,Long> {

    boolean existsByTecnicoIdAndStatus(Long tecnicoId, StatusChamado status);

    Optional<Chamado> findFirstByTecnicoIdAndStatusOrderByDataAberturaAsc(Long tecnicoId, StatusChamado status);

    Optional<Chamado> findFirstByStatusOrderByDataAberturaAsc(StatusChamado status);

    Page<Chamado> findAllByStatusOrderByDataAberturaAsc(StatusChamado status, Pageable pageable);

    Page<Chamado> findAllByTecnicoIdAndStatusInOrderByDataAberturaAsc(Long tecnicoId, List<StatusChamado> statusList, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Chamado c WHERE c.status = 'NA_FILA' AND c.dataAbertura < :dataAbertura")
    Long contarChamadosNaFrente(@Param("dataAbertura") LocalDateTime dataAbertura);

    List<Chamado> findByStatusOrderByDataAberturaAsc(StatusChamado statusChamado);
}
