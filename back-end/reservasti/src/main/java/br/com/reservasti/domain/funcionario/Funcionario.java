package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.endereco.Endereco;
import br.com.reservasti.domain.endereco.EnderecoDTO;
import br.com.reservasti.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_funcionarios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    private Long id;
    private String nome;
    private String email;
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @Embedded
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    public Funcionario(Long id,FuncionarioDTO funcionarioDTO,Departamento departamento) {
        this.id = id;
        this.nome = funcionarioDTO.nome();
        this.email = funcionarioDTO.email();
        this.cpf = funcionarioDTO.cpf();
        this.departamento = departamento;
        this.endereco = new Endereco(funcionarioDTO.endereco());
    }

}
