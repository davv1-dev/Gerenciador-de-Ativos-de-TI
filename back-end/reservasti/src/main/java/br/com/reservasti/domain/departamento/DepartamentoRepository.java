package br.com.reservasti.domain.departamento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    boolean existsByNome(String nome);
    boolean existsByCentroDeCusto(String centroDeCusto);
}
