export interface EquipamentoRetornoDTO {
  id: number;
  nome: string;
  marca: string;
  status: string;
  numeroPatrimonio: string;
  nomeCategoria: string;
  dataSolicitacao: string | Date;
}
export interface EquipamentoDTO {
  nome: string;
  marca: string;
  modelo: string;
  numeroPatrimonio: string;
  categoriaId: number;
}

export interface EditarEquipamentoDTO {
  nome?: string;
  marca?: string;
  modelo?: string;
  categoriaId?: number;
}
export interface AlocarEquipamentoDTO {
  equipamentoId: number;
  departamentoId: number;
}
export interface ItemSimulacaoRequestDTO {
  categoriaId: number;
  quantidadeNecessaria: number;
}

export interface SimulacaoExpansaoDTO {
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
