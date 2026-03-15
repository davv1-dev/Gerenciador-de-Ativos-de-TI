package br.com.reservasti.domain.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank
        String refreshtoken) {
}