package br.com.reservasti.domain.endereco;

public record EditarEnderecoDTO(
          String logradouro
        , String bairro
        , String cep
        , String cidade
        , String uf
        , Integer numero
        , String complemento) {
}
