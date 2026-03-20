import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from 'src/app/core/api/service/toast.service';
import { EquipamentoService } from '../../../core/api/service/equipamento.service';
import { EquipamentoDTO, EditarEquipamentoDTO, EquipamentoRetornoDTO,AlterarStatusDTO } from '../../../core/api/models/equipamento';
import { CategoriariaRetornoDTO } from '../../../core/api/models/categoria';
import { Router } from '@angular/router';

@Component({
  selector: 'app-equipamento-admin',
  templateUrl: './equipamento-admin.component.html',
  styleUrls: ['./equipamento-admin.component.css']
})
export class EquipamentoAdminComponent implements OnInit {

abaAtual: 'cadastro' | 'edicao' | 'categoria' = 'cadastro';  formCadastro: FormGroup;
  formEdicao: FormGroup;
  formBusca: FormGroup;
  formCategoria: FormGroup;

  categorias: CategoriariaRetornoDTO[] = [];
  equipamentosCadastrados: EquipamentoRetornoDTO[] = [];
  equipamentoSelecionadoParaEdicao: EquipamentoRetornoDTO | null = null;

  salvando: boolean = false;
  carregandoLista: boolean = false;
  exibindoModalDesativar: boolean = false;
  equipamentoParaDesativar: EquipamentoRetornoDTO | null = null;
  quantidadeParaDesativar: number = 1;

  exibindoModalStatus: boolean = false;
  equipamentoParaStatus: EquipamentoRetornoDTO | null = null;
  novoStatusSelecionado: string = 'MANUTENCAO';
  quantidadeParaStatus: number = 1;

  constructor(
    private fb: FormBuilder,
    private equipamentoService: EquipamentoService,
    private toastService: ToastService,
    private router: Router
  ) {
    this.formCadastro = this.fb.group({
      nome: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      numeroPatrimonio: ['', Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]],
      categoriaId: ['', Validators.required],
      dataFimGarantia: ['',Validators.required]
    });

    this.formEdicao = this.fb.group({
      nome: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]],
      categoriaId: ['', Validators.required],
      dataFimGarantia: ['']
    });

    this.formBusca = this.fb.group({
      nome: [''],
      categoriaId: ['']
    });

    this.formCategoria = this.fb.group({
      nome: ['', Validators.required],
      descricao:['',Validators.required]
    });
  }

  ngOnInit(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso restrito. Apenas administradores podem gerenciar equipamentos.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return;
    }

    this.carregarCategorias();
    this.carregarEquipamentos();
  }

  carregarCategorias(): void {
    this.equipamentoService.listarCategorias().subscribe({
      next: (page) => {
        this.categorias = page.content;
      },
      error: (err) => console.error('Erro ao carregar categorias', err)
    });
  }

  carregarEquipamentos(): void {
    this.carregandoLista = true;

    const nomeValue:string = this.formBusca.get('nome')?.value;
    const categoriaValue:number = this.formBusca.get('categoriaId')?.value;

    const nome = nomeValue ? nomeValue : undefined;
    const categoriaId = categoriaValue ? Number(categoriaValue) : undefined;

    this.equipamentoService.listarCatalogo(nome, categoriaId).subscribe({
      next: (page) => {
        this.equipamentosCadastrados = page.content;
        this.carregandoLista = false;
      },
      error: (err) => {
        console.error('Erro ao buscar equipamentos', err);
        this.toastService.mostrar('Erro ao buscar equipamentos.', 'erro');
        this.carregandoLista = false;
      }
    });
  }

  limparBusca(): void {
    this.formBusca.reset({ nome: '', categoriaId: '' });
    this.carregarEquipamentos();
  }

  alternarAba(aba: 'cadastro' | 'edicao' | 'categoria') {
    this.abaAtual = aba;
    if (aba === 'edicao') {
      this.equipamentoSelecionadoParaEdicao = null;
      this.carregarEquipamentos();
    }
  }

  salvarNovoEquipamento() {
    if (this.formCadastro.invalid) {
      this.formCadastro.markAllAsTouched();
      return;
    }

    this.salvando = true;
    const dto: EquipamentoDTO = this.formCadastro.value;

    this.equipamentoService.cadastrarEquipamento(dto).subscribe({
      next: (retorno) => {
        this.toastService.mostrar(`Equipamento ${retorno.numeroPatrimonio} cadastrado!`, 'sucesso');
        this.formCadastro.reset();
        this.salvando = false;

        this.carregarEquipamentos();
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao cadastrar equipamento.', 'erro');
        this.salvando = false;
      }
    });
  }
salvarNovaCategoria() {
    if (this.formCategoria.invalid) {
      this.formCategoria.markAllAsTouched();
      return;
    }

    this.salvando = true;
    const dto = this.formCategoria.value;

    this.equipamentoService.cadastrarCategoria(dto).subscribe({
      next: () => {
        this.toastService.mostrar('Categoria cadastrada com sucesso!', 'sucesso');
        this.formCategoria.reset();
        this.carregarCategorias();
        this.salvando = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao cadastrar categoria.', 'erro');
        this.salvando = false;
      }
    });
  }
  selecionarParaEdicao(equipamento: EquipamentoRetornoDTO | null) {
    this.equipamentoSelecionadoParaEdicao = equipamento;

    if (equipamento) {
      let dataFormatada = '';
      if (equipamento.dataFimGarantia) {
        dataFormatada = new Date(equipamento.dataFimGarantia).toISOString().split('T')[0];
      }

      let idCategoriaEncontrada:number | string = '';
      if (equipamento.categoriaNome) {
        const categoriaMatches = this.categorias.find(cat => cat.nome === equipamento.categoriaNome);

        if (categoriaMatches) {
         idCategoriaEncontrada = categoriaMatches.id;
        }
      }

      this.formEdicao.patchValue({
        nome: equipamento.nome,
        marca: equipamento.marca,
        modelo: equipamento.modelo || '',
        quantidade: equipamento.quantidade || 1,
        categoriaId: idCategoriaEncontrada,
        dataFimGarantia: dataFormatada
      });
    } else {
      this.formEdicao.reset();
    }
  }

  salvarEdicao() {
    if (this.formEdicao.invalid || !this.equipamentoSelecionadoParaEdicao) {
      this.formEdicao.markAllAsTouched();
      return;
    }

    this.salvando = true;
    const dto: EditarEquipamentoDTO = this.formEdicao.value;
    const id = this.equipamentoSelecionadoParaEdicao.id;

    this.equipamentoService.editarEquipamento(id, dto).subscribe({
      next: (retorno) => {
        this.toastService.mostrar('Equipamento atualizado com sucesso!', 'sucesso');
        this.equipamentoSelecionadoParaEdicao = null;
        this.carregarEquipamentos();
        this.salvando = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao atualizar equipamento.', 'erro');
        this.salvando = false;
      }
    });
  }

  cancelarEdicao() {
    this.equipamentoSelecionadoParaEdicao = null;
  }

  abrirModalDesativar(equipamento: EquipamentoRetornoDTO, event: Event) {
    event.stopPropagation();
    this.equipamentoParaDesativar = equipamento;
    this.quantidadeParaDesativar = 1;
    this.exibindoModalDesativar = true;
  }

  fecharModalDesativar() {
    this.exibindoModalDesativar = false;
    this.equipamentoParaDesativar = null;
  }

  confirmarDesativacao() {
    if (!this.equipamentoParaDesativar) return;

    const qtdMax = this.equipamentoParaDesativar.quantidade || 1;

    if (this.quantidadeParaDesativar < 1 || this.quantidadeParaDesativar > qtdMax) {
      this.toastService.mostrar(`Quantidade inválida. O máximo disponível é ${qtdMax}.`, 'erro');
      return;
    }

    this.salvando = true;

    this.equipamentoService.desativarEquipamento(this.equipamentoParaDesativar.id, this.quantidadeParaDesativar)
      .subscribe({
        next: () => {
          this.toastService.mostrar('Equipamento(s) desativado(s) com sucesso!', 'sucesso');
          this.fecharModalDesativar();
          this.carregarEquipamentos();
          this.salvando = false;
        },
        error: (err) => {
          console.error(err);
          this.toastService.mostrar('Erro ao desativar equipamento.', 'erro');
          this.salvando = false;
        }
      });
  }
  abrirModalStatus(eqp: EquipamentoRetornoDTO, event: Event): void {
    event.stopPropagation();
    this.equipamentoParaStatus = eqp;
    this.novoStatusSelecionado = eqp.status === 'DISPONIVEL' ? 'MANUTENCAO' : 'DISPONIVEL';
    this.quantidadeParaStatus = eqp.quantidade || 1;
    this.exibindoModalStatus = true;
  }
  fecharModalStatus(): void {
    this.exibindoModalStatus = false;
    this.equipamentoParaStatus = null;
  }
  confirmarAlteracaoStatus(): void {
    if (!this.equipamentoParaStatus) return;
    this.salvando = true;

    const dto: AlterarStatusDTO = {
      statusEquipamento: this.novoStatusSelecionado,
      quantidade: this.equipamentoParaStatus.numeroPatrimonio ? undefined : this.quantidadeParaStatus
    };

    this.equipamentoService.alterarStatus(this.equipamentoParaStatus.id, dto).subscribe({
      next: () => {
        this.fecharModalStatus();
        this.carregarEquipamentos();
        this.salvando = false;
      },
      error: (err) => {
        console.error(err);
        this.salvando = false;
      }
    });
  }
}
