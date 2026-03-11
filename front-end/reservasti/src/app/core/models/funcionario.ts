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
