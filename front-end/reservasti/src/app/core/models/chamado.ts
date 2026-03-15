
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ResumoChamadoDTO {
  id: number;
  nomeSolicitante: string;
  tipoProblema: string;
  localizacao: string;
  status: string;
  descricao: string;
  dataAbertura: string;
}

export interface DetalhamentoChamadoDTO {
  id: number;
  nomeSolicitante: string;
  nomeTecnico: string;
  tipoProblema: string;
  status: string;
  dataAbertura: string;
  posicaoFila?: number;
}
export interface AberturaChamadoDTO {
  equipamentoId?: number | null;
  solicitanteId: number;
  tipoProblema: string;
  descricaoDetalhada: string;
  localizacao: string;
  tecnicoId?: number | null;
}
