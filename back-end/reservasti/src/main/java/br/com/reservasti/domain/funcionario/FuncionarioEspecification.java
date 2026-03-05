package br.com.reservasti.domain.funcionario;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class FuncionarioEspecification {

    public static Specification<Funcionario> comFiltrosFuncionario(String nome, Long departamentoid){
        return(root,query,criteriaBuilder)->{

            List<Predicate> condicoes = new ArrayList<>();

            condicoes.add(criteriaBuilder.notEqual(root.get("ativo"),false));

            if (nome != null && !nome.isBlank()) {
                condicoes.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                ));
            }
            if(departamentoid!=null){
                condicoes.add(criteriaBuilder.equal(
                        root.get("departamento").get("id"),
                        departamentoid
                ));
            }

            return criteriaBuilder.and(condicoes.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

    }
}
