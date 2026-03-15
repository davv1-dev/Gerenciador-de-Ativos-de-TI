package br.com.reservasti.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    UserDetails findByLogin(String username);

    Optional<Usuario> findByloginIgnoreCase(String subject);
}
