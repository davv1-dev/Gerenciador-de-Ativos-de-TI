package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.endereco.Endereco;
import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import br.com.reservasti.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionarios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    private Long id;
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String numeroDeTelefone;
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @Embedded
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    public Funcionario(Long id, FuncionarioDTO funcionarioDTO, Departamento departamento) {
        this.id = id;
        this.nomeCompleto = funcionarioDTO.nome();
        this.email = funcionarioDTO.email();
        this.cpf = funcionarioDTO.cpf();
        this.numeroDeTelefone= funcionarioDTO.numeroDeTelefone();
        this.ativo=true;
        this.departamento = departamento;
        this.endereco = new Endereco(funcionarioDTO.endereco());
    }
    public void atualizarInformacoes(FuncionarioAtualizacaoDTO dto, Departamento novoDepartamento) {
        if (dto.nomeCompleto() != null) this.nomeCompleto = dto.nomeCompleto();
        if (dto.email() != null) this.email = dto.email();
        if (dto.numeroDeTelefone() != null) this.numeroDeTelefone = dto.numeroDeTelefone();
        if (novoDepartamento != null) this.departamento = novoDepartamento;
    }
    public void inativar(){
        this.ativo=false;
    }

}
