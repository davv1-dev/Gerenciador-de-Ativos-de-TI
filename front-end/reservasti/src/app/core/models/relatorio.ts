export interface PrevisaoDemandaDTO {
  nomeCategoria: string;
  quantidadeReservada: number;
}

export interface RelatorioAtrasoDTO {
  nomeFuncionario: string;
  departamento: string;
  patrimonioEquipamento: string;
  diasAtraso: number;
}

export interface RelatorioDeFalhaPorMarcaDTO {
  marca: string;
  modelo: string;
  totalEmManutencao: number;
}

export interface RelatorioDepartamentoDTO {
  nomeDepartamento: string;
  totalEquipamentos: number;
  emUso: number;
  disponiveis: number;
  emManutencao: number;
}

export interface RelatorioGeralDTO {
  totalEquipamentosCadastrados: number;
  totalDisponiveisNoEstoque: number;
  totalEmUsoOuReservados: number;
  totalEmManutencao: number;
}

export interface RelatorioInativosDTO {
  patrimonio: string;
  nomeEquipamento: string;
  departamento: string;
  dataUltimaMovimentacao: string;
}
