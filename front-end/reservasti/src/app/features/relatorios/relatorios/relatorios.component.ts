import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router'; // 👈 Importamos o Router
import { RelatorioService } from 'src/app/core/api/service/relatorio.service';
import { DepartamentoService } from 'src/app/core/api/service/departamento.service';
import { ToastService } from 'src/app/core/api/service/toast.service';
import {
  RelatorioGeralDTO,
  RelatorioDepartamentoDTO,
  RelatorioAtrasoDTO,
  RelatorioDeFalhaPorMarcaDTO,
  PrevisaoDemandaDTO,
  RelatorioInativosDTO
} from '../../../core/api/models/relatorio';
import { DepartamentoRetornoDTO } from '../../../core/api/models/departamento';
import { EquipamentoRetornoDTO } from 'src/app/core/api/models/equipamento';

@Component({
  selector: 'app-relatorios',
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent implements OnInit {

  abaAtual: 'geral' | 'departamentos' | 'riscos' | 'inteligencia' | 'garantias'= 'geral';

  // Dto dos relatorios
  relatorioGeral: RelatorioGeralDTO | null = null;
  relatorioDept: RelatorioDepartamentoDTO | null = null;
  listaAtrasos: RelatorioAtrasoDTO[] = [];
  listaFalhas: RelatorioDeFalhaPorMarcaDTO[] = [];
  listaDemanda: PrevisaoDemandaDTO[] = [];
  listaInativos: RelatorioInativosDTO[] = [];
  listaGarantias: EquipamentoRetornoDTO[] = [];
  totalGarantias: number = 0;
  paginaGarantia: number = 0;


  departamentos: DepartamentoRetornoDTO[] = [];
  carregando: boolean = false;

  // Formulários que viram do html
  formDept: FormGroup;
  formDemanda: FormGroup;
  formInativos: FormGroup;

  constructor(
    private relatorioService: RelatorioService,
    private departamentoService: DepartamentoService,
    private fb: FormBuilder,
    private toastService: ToastService,
    private router: Router
  ) {

    //declarando os atributros dos fomularios:
    this.formDept = this.fb.group({ departamentoId: ['', Validators.required] });

    this.formDemanda = this.fb.group({
      inicio: ['', Validators.required],
      fim: ['', Validators.required]
    });

    this.formInativos = this.fb.group({ dias: [90, [Validators.required, Validators.min(1)]] });
  }

  ngOnInit(): void {
  //assim que abrir a pagina é feita a verificacao do tipo de usuario pra garantir a seguranca foi feito isso em todos:

    const tipoUsuario:string = sessionStorage.getItem('tipoUsuario') || '';
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso negado. Área restrita à diretoria.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return;
    }

    this.carregarRelatorioGeral();
    this.carregarDepartamentos();
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

  gerarRelatorioDept(): void {
    if (this.formDept.invalid) return;
    this.carregando = true;
    const id:number = this.formDept.value.departamentoId;

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

  carregarRiscosEFalhas(): void {
    this.carregando = true;
    this.relatorioService.relatorioAtrasos().subscribe(dados => this.listaAtrasos = dados);
    this.relatorioService.relatorioFalhas().subscribe(dados => {
      this.listaFalhas = dados;
      this.carregando = false;
    });
  }

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
