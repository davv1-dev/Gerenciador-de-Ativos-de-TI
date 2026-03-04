package br.com.reservasti.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Endereco {
    private String logradouro;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;
    private Integer numero;
    private String complemento;

    public Endereco(EnderecoDTO enderecoDTO) {
        this.logradouro = enderecoDTO.logradouro();
        this.bairro = enderecoDTO.bairro();
        this.cep = enderecoDTO.cep();
        this.cidade = enderecoDTO.cidade();
        this.uf = enderecoDTO.uf();
        this.numero = enderecoDTO.numero();
        this.complemento = enderecoDTO.complemento();
    }

    public void atualizarInformacoes(EditarEnderecoDTO enderecoN) {
        if (enderecoN.logradouro() != null) {
            this.logradouro = enderecoN.logradouro();
        }
        if (enderecoN.bairro() != null) {
            this.bairro = enderecoN.bairro();
        }
        if (enderecoN.cep() != null) {
            this.cep = enderecoN.cep();
        }
        if (enderecoN.uf() != null) {
            this.uf = enderecoN.uf();
        }
        if (enderecoN.cidade() != null) {
            this.cidade = enderecoN.cidade();
        }
        if (enderecoN.numero() != null) {
            this.numero = enderecoN.numero();
        }
        if (enderecoN.complemento() != null) {
            this.complemento = enderecoN.complemento();
        }
    }
}
