package br.com.reservasti.domain.equipamento;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoSpecification {

    public static Specification<Equipamento> comFiltros(String nome, Long categoriaId) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> condicoes = new ArrayList<>();

            condicoes.add(criteriaBuilder.notEqual(root.get("status"), StatusEquipamento.BAIXADO));

            if (nome != null && !nome.isBlank()) {
                condicoes.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                ));
            }

            if (categoriaId != null) {
                condicoes.add(criteriaBuilder.equal(
                        root.get("categoria").get("id"),
                        categoriaId
                ));
            }

            return criteriaBuilder.and(condicoes.toArray(new Predicate[0]));
        };
    }
}
