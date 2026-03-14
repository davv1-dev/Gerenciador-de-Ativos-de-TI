import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RelatorioService } from 'src/app/core/service/relatorio.service';
import { DepartamentoService } from 'src/app/core/service/departamento.service';
import { ToastService } from 'src/app/core/service/toast.service';
import {
  RelatorioGeralDTO,
  RelatorioDepartamentoDTO,
  RelatorioAtrasoDTO,
  RelatorioDeFalhaPorMarcaDTO,
  PrevisaoDemandaDTO,
  RelatorioInativosDTO
} from '../../../core/models/relatorio';
import { DepartamentoRetornoDTO } from '../../../core/models/departamento';
import { EquipamentoRetornoDTO } from 'src/app/core/models/equipamento';

@Component({
  selector: 'app-relatorios',
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent implements OnInit {

  abaAtual: 'geral' | 'departamentos' | 'riscos' | 'inteligencia' | 'garantias'= 'geral';

  // Dados dos Relatórios
  relatorioGeral: RelatorioGeralDTO | null = null;
  relatorioDept: RelatorioDepartamentoDTO | null = null;
  listaAtrasos: RelatorioAtrasoDTO[] = [];
  listaFalhas: RelatorioDeFalhaPorMarcaDTO[] = [];
  listaDemanda: PrevisaoDemandaDTO[] = [];
  listaInativos: RelatorioInativosDTO[] = [];
  listaGarantias: EquipamentoRetornoDTO[] = [];
  totalGarantias: number = 0;
  paginaGarantia: number = 0;

  // Apoio
  departamentos: DepartamentoRetornoDTO[] = [];
  carregando: boolean = false;

  // Formulários
  formDept: FormGroup;
  formDemanda: FormGroup;
  formInativos: FormGroup;

  constructor(
    private relatorioService: RelatorioService,
    private departamentoService: DepartamentoService,
    private fb: FormBuilder,
    private toastService: ToastService
  ) {
    this.formDept = this.fb.group({ departamentoId: ['', Validators.required] });

    this.formDemanda = this.fb.group({
      inicio: ['', Validators.required],
      fim: ['', Validators.required]
    });

    this.formInativos = this.fb.group({ dias: [90, [Validators.required, Validators.min(1)]] });
  }

  ngOnInit(): void {
    this.carregarRelatorioGeral();
    this.carregarDepartamentos(); // Para o dropdown da aba 2
  }

  alternarAba(aba: 'geral' | 'departamentos' | 'riscos' | 'inteligencia'|'garantias'): void {
    this.abaAtual = aba;
    if (aba === 'geral' && !this.relatorioGeral) this.carregarRelatorioGeral();
    if (aba === 'riscos' && this.listaAtrasos.length === 0) this.carregarRiscosEFalhas();
    if (aba === 'garantias' && this.listaGarantias.length === 0) this.carregarGarantias();
  }

  private carregarDepartamentos(): void {
    this.departamentoService.listarDepartamentos(0, 100).subscribe({
      next: (page) => this.departamentos = page.content,
      error: () => console.error('Erro ao carregar departamentos para o filtro.')
    });
  }

  // --- ABA 1: GERAL ---
  carregarRelatorioGeral(): void {
    this.carregando = true;
    this.relatorioService.gerarRelatorioGeral().subscribe({
      next: (dados) => {
        this.relatorioGeral = dados;
        this.carregando = false;
      },
      error: () => {
        this.toastService.mostrar('Erro ao carregar visão geral', 'erro');
        this.carregando = false;
      }
    });
  }

  // --- ABA 2: DEPARTAMENTOS ---
  gerarRelatorioDept(): void {
    if (this.formDept.invalid) return;
    this.carregando = true;
    const id = this.formDept.value.departamentoId;

    this.relatorioService.gerarResumoPorDepartamento(id).subscribe({
      next: (dados) => {
        this.relatorioDept = dados;
        this.carregando = false;
      },
      error: () => {
        this.toastService.mostrar('Erro ao carregar dados do departamento', 'erro');
        this.carregando = false;
      }
    });
  }

  // --- ABA 3: RISCOS E FALHAS ---
  carregarRiscosEFalhas(): void {
    this.carregando = true;
    this.relatorioService.relatorioAtrasos().subscribe(dados => this.listaAtrasos = dados);
    this.relatorioService.relatorioFalhas().subscribe(dados => {
      this.listaFalhas = dados;
      this.carregando = false;
    });
  }

  // --- ABA 4: INTELIGÊNCIA ---
  gerarPrevisaoDemanda(): void {
    if (this.formDemanda.invalid) return;
    this.carregando = true;
    const { inicio, fim } = this.formDemanda.value;

    this.relatorioService.previsaoDemanda(inicio, fim).subscribe({
      next: (dados) => {
        this.listaDemanda = dados;
        this.carregando = false;
        if(dados.length === 0) this.toastService.mostrar('Nenhuma reserva no período.', 'info');
      },
      error: () => {
        this.toastService.mostrar('Erro ao gerar previsão.', 'erro');
        this.carregando = false;
      }
    });
  }
  carregarGarantias(): void {
    this.carregando = true;
    this.relatorioService.relatorioGarantiasVencendo(this.paginaGarantia, 10).subscribe({
      next: (page) => {
        this.listaGarantias = page.content;
        this.totalGarantias = page.totalElements;
        this.carregando = false;
      },
      error: () => {
        this.toastService.mostrar('Erro ao carregar garantias.', 'erro');
        this.carregando = false;
      }
    });
  }
mudarPaginaGarantia(novaPagina: number): void {
    this.paginaGarantia = novaPagina;
    this.carregarGarantias();
  }
  gerarRelatorioInativos(): void {
    if (this.formInativos.invalid) return;
    this.carregando = true;
    const dias = this.formInativos.value.dias;

    this.relatorioService.relatorioOciosidade(dias).subscribe({
      next: (dados) => {
        this.listaInativos = dados;
        this.carregando = false;
      },
      error: () => {
        this.toastService.mostrar('Erro ao buscar equipamentos inativos.', 'erro');
        this.carregando = false;
      }
    });
  }
}
