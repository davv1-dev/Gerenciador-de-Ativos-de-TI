package br.com.reservasti.domain.equipamento;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EquipamentoRepository extends JpaRepository<Equipamento,Long>,JpaSpecificationExecutor<Equipamento> {
    boolean existsByNumeroPatrimonio(@NotBlank(message = "O número de patrimônio (etiqueta) é obrigatório") String numeroPatri);

}
