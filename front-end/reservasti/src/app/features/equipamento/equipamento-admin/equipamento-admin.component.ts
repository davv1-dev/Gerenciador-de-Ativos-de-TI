import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from 'src/app/core/service/toast.service';
import { EquipamentoService } from '../../../core/service/equipamento.service';
import { EquipamentoDTO, EditarEquipamentoDTO, EquipamentoRetornoDTO } from '../../../core/models/equipamento';
import { CategoriariaRetornoDTO } from '../../../core/models/categoria'; // Ajuste o path se necessário

@Component({
  selector: 'app-equipamento-admin',
  templateUrl: './equipamento-admin.component.html',
  styleUrls: ['./equipamento-admin.component.css']
})
export class EquipamentoAdminComponent implements OnInit {

  abaAtual: 'cadastro' | 'edicao' = 'cadastro';
  formCadastro: FormGroup;
  formEdicao: FormGroup;
  formBusca: FormGroup;

  categorias: CategoriariaRetornoDTO[] = [];
  equipamentosCadastrados: EquipamentoRetornoDTO[] = [];
  equipamentoSelecionadoParaEdicao: EquipamentoRetornoDTO | null = null;

  salvando: boolean = false;
  carregandoLista: boolean = false;

  constructor(
    private fb: FormBuilder,
    private equipamentoService: EquipamentoService,
    private toastService: ToastService
  ) {
    this.formCadastro = this.fb.group({
      nome: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      numeroPatrimonio: ['', Validators.required],
      categoriaId: ['', Validators.required]
    });

    this.formEdicao = this.fb.group({
      nome: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      categoriaId: ['', Validators.required]
    });
    this.formBusca = this.fb.group({
    nome: [''],
    categoriaId: ['']
  });
  }

  ngOnInit(): void {
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

  const nomeValue = this.formBusca.get('nome')?.value;
  const categoriaValue = this.formBusca.get('categoriaId')?.value;

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
  alternarAba(aba: 'cadastro' | 'edicao') {
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

  // --- EDIÇÃO REAL ---
  selecionarParaEdicao(equipamento: EquipamentoRetornoDTO | null) {
    this.equipamentoSelecionadoParaEdicao = equipamento;

    if (equipamento) {
      this.formEdicao.patchValue({
        nome: equipamento.nome,
        marca: equipamento.marca,
        modelo: '',
        categoriaId: ''
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
}
