package br.com.reservasti.domain.equipamento;

import br.com.reservasti.domain.relatorio.dto.RelatorioDeFalhaPorMarcaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface EquipamentoRepository extends JpaRepository<Equipamento,Long>,JpaSpecificationExecutor<Equipamento> {
    boolean existsByNumeroPatrimonio(@NotBlank(message = "O número de patrimônio (etiqueta) é obrigatório") String numeroPatri);

    boolean existsByCategoriaId(Long id);

    long countByDepartamentoId(Long departamentoId);

    long countByDepartamentoIdAndStatus(Long departamentoId, StatusEquipamento status);

    long countByStatus(StatusEquipamento status);

    @Query("SELECT new br.com.reservasti.domain.relatorio.dto.RelatorioDeFalhaPorMarcaDTO(e.marca, e.modelo, COUNT(e)) " +
            "FROM Equipamento e WHERE e.status = :status " +
            "GROUP BY e.marca, e.modelo ORDER BY COUNT(e) DESC")
    List<RelatorioDeFalhaPorMarcaDTO> findFalhasPorMarca(@Param("status") StatusEquipamento status);

    @Query("SELECT e FROM Equipamento e WHERE e.status = 'DISPONIVEL' AND NOT EXISTS " +
            "(SELECT r FROM Reserva r WHERE r.equipamento = e AND r.dataDevolucaoReal >= :dataLimite)")
    List<Equipamento> findEquipamentosOciosos(@Param("dataLimite") LocalDate dataLimite);

    //long countByCategoriaIdAndDepartamentoIsNullAndStatus(Long categoriaId, StatusEquipamento status);

    Page<Equipamento> findAllByDataFimGarantiaBetween(LocalDate dataInicial,LocalDate dataFinal,Pageable paginacao);

    @Query("SELECT COALESCE(SUM(e.quantidade), 0) FROM Equipamento e WHERE e.categoria.id = :categoriaId AND e.status = :status")
    Integer somarQuantidadeDisponivelPorCategoria(
            @Param("categoriaId") Long categoriaId,
            @Param("status") StatusEquipamento status
    );
}

