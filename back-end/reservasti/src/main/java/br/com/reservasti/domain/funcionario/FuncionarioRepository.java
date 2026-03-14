package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.helpdesk.Chamado;
import br.com.reservasti.domain.helpdesk.StatusChamado;
import br.com.reservasti.domain.usuario.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface FuncionarioRepository extends JpaRepository<Funcionario,Long>, JpaSpecificationExecutor<Funcionario> {
    Page<Funcionario> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByCpf(@NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") String cpf);

    boolean existsByEmail(@NotBlank(message = "O email é obrigatório") @Email(message = "Formato de email inválido") String email);

    boolean existsByDepartamentoId(Long id);

    @Query("SELECT f FROM Funcionario f JOIN Usuario u ON f.id = u.id " +
            "WHERE u.tipoUsuario = :tipoUsuario " +
            "AND f.ativo = true " +
            "AND f.ultimaAtividade > :dataLimite")
    Page<Funcionario> buscarTecnicosOnline(@Param("tipoUsuario") TipoUsuario tipoUsuario,@Param("dataLimite") LocalDateTime dataLimite,Pageable pageable);

    Page<Funcionario> findAllByStatusAcesso(StatusAcesso statusAcesso, Pageable paginacao);

    Page<Funcionario> findByStatusAcessoIn(List<StatusAcesso> status, Pageable paginacao);
}
