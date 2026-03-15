package br.com.reservasti.domain.usuario;


import jakarta.validation.constraints.NotBlank;

public record UsuarioDTOEntrada(@NotBlank String login,@NotBlank String senha) {
}
