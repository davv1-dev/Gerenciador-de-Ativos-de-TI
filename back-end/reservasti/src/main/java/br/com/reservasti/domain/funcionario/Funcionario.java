package br.com.reservasti.domain.funcionario;

import br.com.reservasti.domain.departamento.Departamento;
import br.com.reservasti.domain.endereco.Endereco;
import br.com.reservasti.domain.funcionario.dto.FuncionarioAtualizacaoDTO;
import br.com.reservasti.domain.funcionario.dto.FuncionarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
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
    @Column(name = "ultima_atividade")
    private LocalDateTime ultimaAtividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @Enumerated(EnumType.STRING)
    private StatusAcesso statusAcesso;

    @Embedded
    private Endereco endereco;

    public Funcionario(Long id, FuncionarioDTO funcionarioDTO, Departamento departamento) {
        this.id = id;
        this.nomeCompleto = funcionarioDTO.nomeCompleto();
        this.email = funcionarioDTO.email();
        this.cpf = funcionarioDTO.cpf();
        this.numeroDeTelefone= funcionarioDTO.numeroDeTelefone();
        this.ativo=false;
        this.departamento = departamento;
        this.statusAcesso = StatusAcesso.PENDENTE;
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

    public void aprovarAcesso() {
        this.statusAcesso = StatusAcesso.APROVADO;
        this.ativo=true;
    }

    public void negarAcesso() {
        this.statusAcesso = StatusAcesso.NEGADO;
    }
}
