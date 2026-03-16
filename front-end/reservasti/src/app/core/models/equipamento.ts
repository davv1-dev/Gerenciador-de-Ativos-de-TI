export interface EquipamentoRetornoDTO {
  id: number;
  nome: string;
  marca: string;
  modelo: string;
  status: string;
  numeroPatrimonio: string;
  quantidade: number;
  categoriaNome: string;
  dataFimGarantia: string | Date;
}
export interface EquipamentoDTO {
  nome: string;
  marca: string;
  quantidade?: number;
  numeroPatrimonio: string;
  categoriaId: number;
  dataFimGarantia: string | Date;
}

export interface EditarEquipamentoDTO {
  nome?: string;
  marca?: string;
  modelo?: string;
  categoriaId?: number;
  dataFimGarantia?: string | Date;
  quantidade?:number

}
export interface AlocarEquipamentoDTO {
  idEquipamento: number;
  idDepartamento: number;
  quantidade?: number;
}
export interface ItemSimulacaoRequestDTO {
  categoriaId: number;
  quantidadeNecessaria: number;
}

export interface SimulacaoEquipamentosDTO {
  itens: ItemSimulacaoRequestDTO[];
}

export interface ItemResultadoSimulacaoDTO {
  categoriaId: number;
  nomeCategoria: string;
  quantidadeNecessaria: number;
  quantidadeDisponivel: number;
  quantidadeFaltante: number;
  viavel: boolean;
}

export interface ResultadoSimulacaoDTO {
  expansaoTotalmenteViavel: boolean;
  detalhes: ItemResultadoSimulacaoDTO[];
}
export interface AlterarStatusDTO{
  statusEquipamento:string
  quantidade?:number
  }

