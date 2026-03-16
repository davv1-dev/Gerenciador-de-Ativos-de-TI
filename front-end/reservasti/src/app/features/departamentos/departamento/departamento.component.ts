import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DepartamentoService } from 'src/app/core/service/departamento.service';
import { ToastService } from 'src/app/core/service/toast.service';
import { ConfirmDialogService } from 'src/app/core/service/confirm-dialog.service';
import { DepartamentoRetornoDTO, DepartamentoDTO, DepartamentoAtualizacaoDTO } from '../../../core/models/departamento';
import { Router } from '@angular/router';

@Component({
  selector: 'app-departamento',
  templateUrl: './departamento.component.html',
  styleUrls: ['./departamento.component.css']
})
export class DepartamentoComponent implements OnInit {

  abaAtual: 'cadastro' | 'gerenciar' = 'cadastro';

  departamentosCadastrados: DepartamentoRetornoDTO[] = [];
  departamentoSelecionadoParaEdicao: DepartamentoRetornoDTO | null = null;

  formCadastro: FormGroup;
  formEdicao: FormGroup;

  carregandoLista: boolean = false;
  salvando: boolean = false;

  constructor(
    private fb: FormBuilder,
    private departamentoService: DepartamentoService,
    private toastService: ToastService,
    private confirmDialogService: ConfirmDialogService,
    private router: Router
  ) {
    this.formCadastro = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      centroDeCusto: ['', Validators.required]
    });

    this.formEdicao = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      centroDeCusto: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const tipoUsuario = sessionStorage.getItem('tipoUsuario');
    if (tipoUsuario !== 'ADMIN') {
      this.toastService.mostrar('Acesso restrito. Área exclusiva da diretoria.', 'erro');

      if (tipoUsuario === 'TECNICO') {
        this.router.navigate(['/home-tecnico']);
      } else {
        this.router.navigate(['/home']);
      }
      return;
    }

    this.carregarDepartamentos();
  }

  alternarAba(aba: 'cadastro' | 'gerenciar'): void {
    this.abaAtual = aba;
    if (aba === 'gerenciar') {
      this.cancelarEdicao();
      this.carregarDepartamentos();
    }
  }

  carregarDepartamentos(): void {
    this.carregandoLista = true;
    this.departamentoService.listarDepartamentos(0, 50).subscribe({
      next: (page) => {
        this.departamentosCadastrados = page.content;
        this.carregandoLista = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao carregar departamentos', 'erro');
        this.carregandoLista = false;
      }
    });
  }

  salvarNovoDepartamento(): void {
    if (this.formCadastro.invalid) {
      this.toastService.mostrar('Preencha os campos obrigatórios.', 'info');
      return;
    }

    this.salvando = true;
    const dto: DepartamentoDTO = this.formCadastro.value;

    this.departamentoService.cadastrarDepartamento(dto).subscribe({
      next: () => {
        this.toastService.mostrar('Departamento cadastrado com sucesso!', 'sucesso');
        this.formCadastro.reset();
        this.salvando = false;
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao cadastrar departamento.', 'erro');
        this.salvando = false;
      }
    });
  }

  selecionarParaEdicao(dep: DepartamentoRetornoDTO): void {
    this.departamentoSelecionadoParaEdicao = dep;
    this.formEdicao.patchValue({
      nome: dep.nome,
      centroDeCusto: dep.centroDeCusto
    });
  }

  cancelarEdicao(): void {
    this.departamentoSelecionadoParaEdicao = null;
    this.formEdicao.reset();
  }

  salvarEdicao(): void {
    if (this.formEdicao.invalid || !this.departamentoSelecionadoParaEdicao) {
      this.toastService.mostrar('Preencha os campos obrigatórios.', 'info');
      return;
    }

    this.salvando = true;
    const dto: DepartamentoAtualizacaoDTO = this.formEdicao.value;

    this.departamentoService.atualizarDepartamento(this.departamentoSelecionadoParaEdicao.id, dto).subscribe({
      next: () => {
        this.toastService.mostrar('Departamento atualizado com sucesso!', 'sucesso');
        this.salvando = false;
        this.cancelarEdicao();
        this.carregarDepartamentos();
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao atualizar departamento.', 'erro');
        this.salvando = false;
      }
    });
  }

  async excluirDepartamento(dep: DepartamentoRetornoDTO, event: Event) {
    event.stopPropagation();

    const confirmou = await this.confirmDialogService.confirmar(
      'Excluir Departamento',
      `Tem certeza que deseja excluir o departamento ${dep.nome}?`
    );

    if (!confirmou) return;

    this.departamentoService.excluirDepartamento(dep.id).subscribe({
      next: () => {
        this.toastService.mostrar('Departamento excluído com sucesso.', 'sucesso');
        this.carregarDepartamentos();
      },
      error: (err) => {
        console.error(err);
        this.toastService.mostrar('Erro ao excluir. Podem existir vínculos associados.', 'erro');
      }
    });
  }
}
