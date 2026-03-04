package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.endereco.Endereco;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Funcionario {
    @Id
    private long id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String numeroDeTelefone;
    @Embedded
    private Endereco endereço;
}
