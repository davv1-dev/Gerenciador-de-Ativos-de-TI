package br.com.reservasti.domain.funcionario.dto;

import br.com.reservasti.domain.endereco.EnderecoDTO;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;


public record FuncionarioDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,
        @Pattern(regexp = "\\d{2} \\d{5}-\\d{4}")
        String numeroDeTelefone,

        @NotNull(message = "O ID do departamento é obrigatório")
        Long departamentoId,

        @NotNull(message = "Os dados de endereço são obrigatórios")
        @Valid
        EnderecoDTO endereco
) {
}
