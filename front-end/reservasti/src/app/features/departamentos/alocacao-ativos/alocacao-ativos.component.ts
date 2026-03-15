import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EquipamentoService } from 'src/app/core/service/equipamento.service';
import { DepartamentoService } from 'src/app/core/service/departamento.service';
import { ToastService } from 'src/app/core/service/toast.service';
import { ConfirmDialogService } from 'src/app/core/service/confirm-dialog.service';
import { Router } from '@angular/router'; // 👈 Importamos o Router

import {
  EquipamentoRetornoDTO,
  AlocarEquipamentoDTO,
  SimulacaoEquipamentosDTO,
  ResultadoSimulacaoDTO,
  ItemSimulacaoRequestDTO
} from '../../../core/models/equipamento';
import { DepartamentoRetornoDTO } from '../../../core/models/departamento';
import { CategoriariaRetornoDTO } from '../../../core/models/categoria';

@Component({
  selector: 'app-alocacao-ativos',
  templateUrl: './alocacao-ativos.component.html',
  styleUrls: ['./alocacao-ativos.component.css']
})
export class AlocacaoAtivosComponent implements OnInit {

  abaAtiva: 'alocacao' | 'expansao' = 'alocacao';

  departamentos: DepartamentoRetornoDTO[] = [];
  equipamentosBusca: EquipamentoRetornoDTO[] = [];
  departamentoSelecionado: DepartamentoRetornoDTO | null = null;
  equipamentoSelecionado: EquipamentoRetornoDTO | null = null;
  formBuscaEquipamento: FormGroup;
  quantidadeAlocacao: number = 1;
  buscando: boolean = false;
  alocando: boolean = false;

  tipoExpansao: 'novo' | 'existente' = 'novo';
  departamentoExpansaoSelecionado: DepartamentoRetornoDTO | null = null;

  itensParaSimular: { categoriaId: number; nomeCategoria: string; quantidade: number }[] = [];

  simulando: boolean = false;
  resultadoSimulacao: ResultadoSimulacaoDTO | null = null;

  constructor(
    private fb: FormBuilder,
    private equipamentoService: EquipamentoService,
    private departamentoService: DepartamentoService,
    private confirmDialogService: ConfirmDialogService,
    private toastService: ToastService,
    private router: Router // 👈 Injetamos o Router aqui
  ) {
    this.formBuscaEquipamento = this.fb.group({
      nomeOuPatrimonio: ['']
    });
  }

  ngOnInit(): void {
    // 👇 LEÃO DE CHÁCARA: Apenas Admins podem acessar alocação e expansão!
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso restrito. Área exclusiva da administração.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return; // Impede que as chamadas a serviços sejam feitas
    }

    this.carregarDepartamentos();
    this.carregarCategoriasParaSimulacao();
  }

  mudarAba(aba: 'alocacao' | 'expansao'): void {
    this.abaAtiva = aba;
  }

  carregarDepartamentos(): void {
    this.departamentoService.listarDepartamentos(0, 100).subscribe({
      next: (page) => this.departamentos = page.content,
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao carregar os departamentos.', 'erro');
      }
    });
  }

  selecionarDepartamento(dep: DepartamentoRetornoDTO): void {
    if (this.abaAtiva === 'alocacao') {
      this.departamentoSelecionado = dep;
    } else {
      this.departamentoExpansaoSelecionado = dep;
    }
  }

  buscarEquipamento(): void {
    this.buscando = true;
    const termo = this.formBuscaEquipamento.get('nomeOuPatrimonio')?.value;

    this.equipamentoService.listarCatalogo(termo, undefined).subscribe({
      next: (page) => {
        this.equipamentosBusca = page.content;
        this.buscando = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao buscar equipamentos.', 'erro');
        this.buscando = false;
      }
    });
  }

  selecionarEquipamentoParaAlocar(eqp: EquipamentoRetornoDTO): void {
    this.equipamentoSelecionado = eqp;
    this.quantidadeAlocacao = 1; // 👈 Reseta para 1 ao escolher novo item
  }

  async confirmarAlocacao() {
    if (!this.departamentoSelecionado || !this.equipamentoSelecionado) return;

    // Pega o patrimônio ou o nome (se for lote)
    const identificador = this.equipamentoSelecionado.numeroPatrimonio || this.equipamentoSelecionado.nome;

    const confirmou = await this.confirmDialogService.confirmar(
      'Confirmar Alocação',
      `Deseja alocar ${this.quantidadeAlocacao}x [${identificador}] para o departamento ${this.departamentoSelecionado.nome}?`
    );

    if (!confirmou) return;

    this.alocando = true;
    const dto: AlocarEquipamentoDTO = {
      equipamentoId: this.equipamentoSelecionado.id,
      departamentoId: this.departamentoSelecionado.id,
      quantidade: this.quantidadeAlocacao // 👈 Mandando a quantidade pro Back!
    };

    this.equipamentoService.alocarAoDepartamento(dto).subscribe({
      next: () => {
        this.confirmDialogService.confirmar('Sucesso!', 'Equipamento alocado!');
        this.alocando = false;
        this.equipamentoSelecionado = null;
        this.equipamentosBusca = [];
      },
      error: (err) => {
        console.error(err);
        this.confirmDialogService.confirmar('Erro', 'Não foi possível alocar.');
        this.alocando = false;
      }
    });
  }

  carregarCategoriasParaSimulacao(): void {
    this.equipamentoService.listarCategorias().subscribe({
      next: (page) => {
        this.itensParaSimular = page.content.map(cat => ({
          categoriaId: cat.id,
          nomeCategoria: cat.nome,
          quantidade: 0
        }));
      },
      error: (err) => console.error('Erro ao carregar categorias', err)
    });
  }

  aumentarQuantidade(item: any): void { item.quantidade++; }
  diminuirQuantidade(item: any): void { if (item.quantidade > 0) item.quantidade--; }

  simularViabilidade(): void {
    const itensRequisitados: ItemSimulacaoRequestDTO[] = this.itensParaSimular
      .filter(item => item.quantidade > 0)
      .map(item => ({
        categoriaId: item.categoriaId,
        quantidadeNecessaria: item.quantidade
      }));

    if (itensRequisitados.length === 0) {
      this.toastService.mostrar('Adicione pelo menos um equipamento para simular.', 'info');
      return;
    }

    this.simulando = true;
    const request: SimulacaoEquipamentosDTO = { itens: itensRequisitados };

    this.equipamentoService.simularExpansao(request).subscribe({
      next: (resultado) => {
        this.resultadoSimulacao = resultado;
        this.simulando = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao realizar a simulação.', 'erro');
        this.simulando = false;
      }
    });
  }

  novaSimulacao(): void {
    this.resultadoSimulacao = null;
    this.itensParaSimular.forEach(item => item.quantidade = 0);
  }
}
