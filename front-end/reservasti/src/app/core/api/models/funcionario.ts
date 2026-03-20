export interface EnderecoDTO {
  logradouro: string;
  bairro: string;
  cep: string;
  cidade: string;
  uf: string;
  numero?: number;
  complemento?: string;
}

export interface FuncionarioDTO {
  nomeCompleto: string;
  email: string;
  cpf: string;
  numeroDeTelefone: string;
  departamentoId: number;
  endereco: EnderecoDTO;
}
export interface FuncionarioRetornoDTO {
  id: number;
  nomeCompleto: string;
  email: string,
  cpf: string ,
  numeroDeTelefone: string ,
  nomeDepartamento: string ,
  ativo: boolean
  statusAcesso?: string;
  dataSolicitacao?: string | Date

}
export interface FuncionarioAtualizacaoDTO{
nomeCompleto: string ,
email: string,
numeroDeTelefone: string,
departamentoId: number
}



