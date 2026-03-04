package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.endereco.EnderecoDTO;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record FuncionarioDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotNull(message = "O ID do departamento é obrigatório")
        Long departamentoId,

        @NotNull(message = "Os dados de endereço são obrigatórios")
        @Valid
        EnderecoDTO endereco
) {
}
