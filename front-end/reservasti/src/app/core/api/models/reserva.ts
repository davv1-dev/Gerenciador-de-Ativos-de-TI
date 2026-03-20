export interface ReservaDTO {
  funcionarioId?: number;
  equipamentoId: number;
  dataPrevistaRetirada: string;
  dataPrevistaDevolucao: string;
}

export interface ReservaRetornoDTO {
  id: number;
  nomeFuncionario: string;
  nomeEquipamento: string;
  dataPrevistaRetirada: string;
  dataPrevistaDevolucao: string;
  status: string;
}
