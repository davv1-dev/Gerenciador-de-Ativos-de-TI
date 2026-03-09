package br.com.reservasti.domain.helpdesk;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<Chamado,Long> {

    boolean existsByTecnicoIdAndStatus(Long tecnicoId, StatusChamado status);

    Optional<Chamado> findFirstByTecnicoIdAndStatusOrderByDataAberturaAsc(Long tecnicoId, StatusChamado status);

    Optional<Chamado> findFirstByStatusOrderByDataAberturaAsc(StatusChamado status);

    List<Chamado> findAllByStatusOrderByDataAberturaAsc(StatusChamado status);

    List<Chamado> findAllByTecnicoIdAndStatusInOrderByDataAberturaAsc(Long tecnicoId, List<StatusChamado> statusList);
}
