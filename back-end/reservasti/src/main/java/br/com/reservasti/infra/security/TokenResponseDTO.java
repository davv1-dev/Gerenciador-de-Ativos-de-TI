package br.com.reservasti.infra.security;

import br.com.reservasti.domain.usuario.TipoUsuario;

public record TokenResponseDTO(String tokenJWT, String refreshToken, TipoUsuario tipoUsuario) {
}
