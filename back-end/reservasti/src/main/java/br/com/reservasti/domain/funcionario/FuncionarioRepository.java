package br.com.reservasti.domain.funcionario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FuncionarioRepository extends JpaRepository<Funcionario,Long> {
    Page<Funcionario> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByCpf(@NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") String cpf);

    boolean existsByEmail(@NotBlank(message = "O email é obrigatório") @Email(message = "Formato de email inválido") String email);
}
